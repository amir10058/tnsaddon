package de.ampada.tmsaddon.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import de.ampada.tmsaddon.configuration.jwt.JwtTokenProvider;
import de.ampada.tmsaddon.domains.Role;
import de.ampada.tmsaddon.domains.User;
import de.ampada.tmsaddon.domains.UserRole;
import de.ampada.tmsaddon.dto.UserRegisterDTO;
import de.ampada.tmsaddon.enums.RoleEnum;
import de.ampada.tmsaddon.exception.CustomException;
import de.ampada.tmsaddon.mappers.UserMapper;
import de.ampada.tmsaddon.repository.UserRepository;
import de.ampada.tmsaddon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;


    @Override
    public String login(String username, String password) {
        LOGGER.info("login. method init. username:{}", username);

        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            /*
             *BAGHERI: ALTHOUGH THIS KIND OF CONDITION WILL NOT HAPPENED IN THIS KIND OF PROJECT AND THIS METHOD IS ONLY
             *ACCESSIBLE FROM USERCONTROLLER AND WE DON'T PASS THROUGH BLANK USERNAME OR PASSWORD BUT WE MUST HAVE VALIDTION IN ALL LAYERS.
             **/
            LOGGER.error("login.username or password is blanked or not supplied. username:{}, password:{}", username, password);
            throw new CustomException("username or password is blanked or not supplied");
        }

        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            LOGGER.error("login.Invalid username/password supplied. username:{}, password:{}", username, password);
            throw new CustomException("Invalid username/password supplied");
        }
        if (authenticate != null && !CollectionUtils.isEmpty(authenticate.getAuthorities())) {
            LOGGER.debug("login. username {} has logged in successfully. his/her roles is:{}",
                    username,
                    authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

            return jwtTokenProvider.createToken(username, authenticate.getAuthorities());
        }
        LOGGER.error("abnormal error happened. maybe we have kind of DB connection problem!");
        throw new CustomException("abnormal error happened. maybe we have kind of DB connection problem!");
    }

    @Override
    public String register(UserRegisterDTO userRegisterDTO) {
        try {
            LOGGER.info("register. method init. userRegisterDTO:{}", objectMapper.writeValueAsString(userRegisterDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (userRegisterDTO == null ||
                Strings.isNullOrEmpty(userRegisterDTO.getUsername()) ||
                Strings.isNullOrEmpty(userRegisterDTO.getPassword())) {
            /*
             *BAGHERI: ALTHOUGH THIS KIND OF CONDITION WILL NOT HAPPENED IN THIS KIND OF PROJECT AND THIS METHOD IS ONLY
             *ACCESSIBLE FROM USERCONTROLLER AND WE DON'T PASS THROUGH BLANK USERNAME OR PASSWORD BUT WE MUST HAVE VALIDTION IN ALL LAYERS.
             **/
            try {
                LOGGER.error("register.userRegisterDTO is null or its username or password is blanked. userRegisterDTO:{}",
                        objectMapper.writeValueAsString(userRegisterDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("userRegisterDTO is null or its username or password is blanked");
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            LOGGER.error("register. user is currently registered. username:{}", userRegisterDTO.getUsername());
            throw new CustomException("user is currently registered");
        }
        User userBeforePersist = userMapper.convertUserRegisterDTOToUser(userRegisterDTO);
        userBeforePersist.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        /*BAGHERI: THESE ARE DEFAULT ROLES FOR REGISTERED CUSTOMER*/
        Set<UserRole> userRoleSet = Stream.of(RoleEnum.ROLE_USER.name(), RoleEnum.ROLE_DUMMY.name())
                .distinct()
                .map(Role::new)
                .map(UserRole::new)
                .collect(Collectors.toSet());

        userBeforePersist.setUserRolesList(userRoleSet);
        userRepository.save(userBeforePersist);

        LOGGER.debug("register. user has registered successfully. username:{}", userRegisterDTO.getUsername());

        return jwtTokenProvider.createToken(userBeforePersist.getUsername(), userRoleSet);
    }
}
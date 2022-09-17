package de.ampada.tmsaddon.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import de.ampada.tmsaddon.configuration.jwt.JwtTokenProvider;
import de.ampada.tmsaddon.dtos.UserDTO;
import de.ampada.tmsaddon.dtos.UserRegisterDTO;
import de.ampada.tmsaddon.entities.Role;
import de.ampada.tmsaddon.entities.User;
import de.ampada.tmsaddon.entities.UserRole;
import de.ampada.tmsaddon.enums.RoleEnum;
import de.ampada.tmsaddon.exception.CustomException;
import de.ampada.tmsaddon.mappers.UserMapper;
import de.ampada.tmsaddon.repository.UserRepository;
import de.ampada.tmsaddon.services.UserService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

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
    public UserDTO getById(String id) {
        LOGGER.info("getById. method init. id:{}", id);

        if (Strings.isNullOrEmpty(id)) {
            LOGGER.error("getById. id is null or empty. id:{}", id);
            throw new CustomException("id is null or empty.");
        }

        Optional<User> optionalUserById = userRepository.findById(new ObjectId(id));
        if (!optionalUserById.isPresent()) {
            LOGGER.error("getById.id is not present and is invalid. id:{}", id);
            throw new CustomException("id is not present and is invalid");
        }

        return userMapper.convertEntityToDTO(optionalUserById.get());
    }

    @Override
    public UserDTO getByUsername(String username) {
        LOGGER.info("getByUsername. method init. username:{}", username);

        if (Strings.isNullOrEmpty(username)) {
            LOGGER.error("getByUsername. username is null or empty. username:{}", username);
            throw new CustomException("username is null or empty");
        }

        User userEntityByUsername = userRepository.findUserByUsername(username);
        if (userEntityByUsername == null) {
            LOGGER.error("getByUsername.username is not present and is invalid. username:{}", username);
            throw new CustomException("username is not present and is invalid");
        }

        return userMapper.convertEntityToDTO(userEntityByUsername);
    }

    @Override
    public List<UserDTO> getList() {
        LOGGER.info("getList.method init.");
        List<User> allUserEntityList = userRepository.findAll();
        if (CollectionUtils.isEmpty(allUserEntityList)) {
            LOGGER.error("getList.no user found in DB.");
            throw new CustomException("no user found in DB.");
        }
        LOGGER.debug("getList. {} user found in DB.", allUserEntityList.size());
        return userMapper.convertEntitiesToDTOs(allUserEntityList);
    }

    @Override
    public UserDTO getCurrentUserDTO() {
        UserDTO currentLoginUserDTO = null;
        try {
            String currentLoginUsername = ((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            currentLoginUserDTO = this.getByUsername(currentLoginUsername);
        } catch (Exception e) {
            LOGGER.error("getCurrentUserDTO. abnormal error happened! ex.msg:{}", e.getMessage());
        }
        if (currentLoginUserDTO == null) {
            LOGGER.error("getCurrentUserDTO. could not extract currentLoginUserDTO from DB!");
            throw new CustomException("could not extract currentLoginUserDTO from DB!");
        }
        return currentLoginUserDTO;
    }

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
    public UserDTO register(UserRegisterDTO userRegisterDTO) {
        try {
            LOGGER.info("register. method init. userDTO:{}", objectMapper.writeValueAsString(userRegisterDTO));
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
                LOGGER.error("register.userDTO is null or its username or password is blanked. userDTO:{}",
                        objectMapper.writeValueAsString(userRegisterDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            throw new CustomException("userDTO is null or its username or password is blanked");
        }
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            LOGGER.error("register. user is currently registered. username:{}", userRegisterDTO.getUsername());
            throw new CustomException("user is currently registered");
        }
        User userEntityFromDTO = userMapper.convertUserRegisterDTOToEntity(userRegisterDTO);
        userEntityFromDTO.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        /*BAGHERI: THESE ARE DEFAULT ROLES FOR REGISTERED CUSTOMER*/
        Set<UserRole> userRoleSet = Stream.of(RoleEnum.ROLE_USER.name(), RoleEnum.ROLE_DUMMY.name())
                .distinct()
                .map(Role::new)
                .map(UserRole::new)
                .collect(Collectors.toSet());

        userEntityFromDTO.setUserRoleSet(userRoleSet);
        User userEntityAfterCreate = userRepository.save(userEntityFromDTO);

        LOGGER.debug("register. user has registered successfully. username:{}", userRegisterDTO.getUsername());
        return userMapper.convertEntityToDTO(userEntityAfterCreate);
    }
}

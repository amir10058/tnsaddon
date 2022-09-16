package de.ampada.tmsaddon.configuration.jwt;

import de.ampada.tmsaddon.configuration.security.MyUserDetails;
import de.ampada.tmsaddon.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {
    /**
     * BAGHERI: THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    private MyUserDetails myUserDetails;

    @PostConstruct
    /*BAGHERI: THIS PART ADDED FOR EXTRA SECURITY*/
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //    public String createToken(String username, Set<UserRole> userRoleSet) {
    public String createToken(String username, Collection<? extends GrantedAuthority> grantedAuthorityCollection) {

        Claims claims = Jwts.claims().setSubject(username);
//        if (!CollectionUtils.isEmpty(userRoleSet))
//            claims.put("auth", userRoleSet.stream()
//                    .map(UserRole::getRole)
//                    .filter(Objects::nonNull)
//                    .map(role -> new SimpleGrantedAuthority(role.getName()))
//                    .collect(Collectors.toList())
//            );
        claims.put("auth", grantedAuthorityCollection);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token");
        }
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

}

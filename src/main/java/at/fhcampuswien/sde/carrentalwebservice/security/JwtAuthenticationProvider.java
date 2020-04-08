package at.fhcampuswien.sde.carrentalwebservice.security;

import io.jsonwebtoken.JwtException;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Vector;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtTokenService jwtService;

    @SuppressWarnings("unused")
    public JwtAuthenticationProvider() {
        this(null);
    }

    @Autowired
    public JwtAuthenticationProvider(JwtTokenService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            String token = (String) authentication.getCredentials();
            String username = jwtService.getUsernameFromToken(token);

            /*
            return jwtService.validateToken(token)
                    .map(aBoolean -> new JwtAuthenticatedProfile(username))
                    .orElseThrow(() -> new JwtAuthenticationException("JWT Token validation failed"));
            */

            Optional<Boolean> optTokenValidity = jwtService.validateToken(token);
            if (!optTokenValidity.isPresent()) {
                throw new JwtAuthenticationException("JWT Token validation failed");
            }

            if (!optTokenValidity.get()) {
                throw new JwtAuthenticationException("JWT Token validation failed");
            }

            /*
            log.info(authentication.toString());

            authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info(authentication.toString());

            if(authentication instanceof JwtAuthentication) {
                JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
                User user = new User(username, "", new Vector<GrantedAuthority>());
                jwtAuthentication.setPrincipal(user);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }
            */

            return new JwtAuthenticatedProfile(username);

        } catch (JwtException ex) {
            log.error(String.format("Invalid JWT Token: %s", ex.getMessage()));
            throw new JwtAuthenticationException("Failed to verify token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.equals(authentication);
    }
}

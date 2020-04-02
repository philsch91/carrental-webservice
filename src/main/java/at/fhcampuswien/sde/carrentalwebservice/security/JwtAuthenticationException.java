package at.fhcampuswien.sde.carrentalwebservice.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String message){
        super(message);
    }
}

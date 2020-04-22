package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.exception.AuthenticationForbiddenException;
import at.fhcampuswien.sde.carrentalwebservice.security.JwtAuthenticatedProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseRestController {

    private static final Logger log = LoggerFactory.getLogger(BaseRestController.class);

    protected JwtAuthenticatedProfile getAuthentication() throws AuthenticationForbiddenException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof JwtAuthenticatedProfile)) {
            throw new AuthenticationForbiddenException("authentication failure");
        }

        log.info(auth.toString());
        log.info(Boolean.toString(auth.isAuthenticated()));
        log.info(auth.getName());
        log.info(auth.getPrincipal().toString());

        JwtAuthenticatedProfile authenticatedProfile = (JwtAuthenticatedProfile) auth;
        //String userEmail = authenticatedProfile.getName();

        return authenticatedProfile;
    }
}

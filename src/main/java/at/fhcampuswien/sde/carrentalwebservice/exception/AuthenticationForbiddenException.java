package at.fhcampuswien.sde.carrentalwebservice.exception;

import at.fhcampuswien.sde.carrentalwebservice.model.User;

public class AuthenticationForbiddenException extends RuntimeException {

    public AuthenticationForbiddenException (String message) {
        super(message);
    }

    public AuthenticationForbiddenException (User user){
        super("User " + user.getEmail() + " forbidden");
    }
}

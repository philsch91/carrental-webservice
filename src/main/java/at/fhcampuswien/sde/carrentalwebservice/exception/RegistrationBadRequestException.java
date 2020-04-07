package at.fhcampuswien.sde.carrentalwebservice.exception;

public class RegistrationBadRequestException extends RuntimeException {

    public RegistrationBadRequestException(){
        super("Incorrect registration data");
    }
    public RegistrationBadRequestException (String message) {
        super(message);
    }

}

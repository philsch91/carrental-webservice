package at.fhcampuswien.sde.carrentalwebservice.model.response;

public class GenericResponse {

    private int status;
    private String message;

    public GenericResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

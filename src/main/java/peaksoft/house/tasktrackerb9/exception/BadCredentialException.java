package peaksoft.house.tasktrackerb9.exception;

public class BadCredentialException extends RuntimeException{

    public BadCredentialException() {
    }

    public BadCredentialException(String message) {
        super(message);
    }
}

package peaksoft.house.tasktrackerb9.exception;

public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException() {
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}

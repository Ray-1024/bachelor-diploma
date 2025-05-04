package ray1024.articleservice.exception;

public class WrongAuthorException extends RuntimeException {
    public WrongAuthorException(String message) {
        super(message);
    }
}

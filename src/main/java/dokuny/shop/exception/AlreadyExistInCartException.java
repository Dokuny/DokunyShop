package dokuny.shop.exception;

public class AlreadyExistInCartException extends RuntimeException{
    public AlreadyExistInCartException(String message) {
        super(message);
    }
}

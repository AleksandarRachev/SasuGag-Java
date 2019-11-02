package test.demo.exception;

public class ElementMissingException extends RuntimeException {
    public ElementMissingException(String message) {
        super(message);
    }
}

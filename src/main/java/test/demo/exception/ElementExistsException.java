package test.demo.exception;

public class ElementExistsException extends RuntimeException {
    public ElementExistsException(String message) {
        super(message);
    }
}

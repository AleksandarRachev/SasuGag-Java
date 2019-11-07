package test.demo.exception;

public class UnsupportedImageFormatException extends RuntimeException {
    public UnsupportedImageFormatException(String message) {
        super(message);
    }
}

package test.demo.exception;

public class ImageMissingException extends RuntimeException {
    public ImageMissingException(String message) {
        super(message);
    }
}

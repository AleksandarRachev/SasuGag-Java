package gag.sasu.exception;

public class CannotDeletePostException extends RuntimeException {
    public CannotDeletePostException() {
        super("You cannot delete this post");
    }
}

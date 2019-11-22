package gag.sasu.exception;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
        super("Wrong credentials");
    }
}

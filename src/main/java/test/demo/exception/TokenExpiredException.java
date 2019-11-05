package test.demo.exception;

import java.io.IOException;

public class TokenExpiredException extends IOException {
    public TokenExpiredException(String tokenExpired) {
    }
}

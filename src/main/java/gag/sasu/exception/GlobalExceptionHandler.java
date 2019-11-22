package gag.sasu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, ElementExistsException.class, ElementMissingException.class,
            UnsupportedImageFormatException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleBadRequest(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WrongCredentialsException.class, TokenExpiredException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> handleForbidden(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleMultipartRequest(MissingServletRequestPartException e) {
        String name = e.getRequestPartName();
        String s1 = name.substring(0, 1).toUpperCase();
        name = s1 + name.substring(1);
        return new ResponseEntity<>(new ErrorMessage(name + " must not be empty."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MultipartException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleMultipartRequest(MultipartException e) {
        return new ResponseEntity<>(new ErrorMessage("File must not be empty."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleMissingServletException(MissingServletRequestParameterException e) {
        String name = e.getParameterName();
        String s1 = name.substring(0, 1).toUpperCase();
        name = s1 + name.substring(1);
        return new ResponseEntity<>(new ErrorMessage(name + " must not be empty."), HttpStatus.BAD_REQUEST);
    }
}

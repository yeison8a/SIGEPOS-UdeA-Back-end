package edu.udea.sigepos.exception;

import edu.udea.sigepos.service.AuthService;
import edu.udea.sigepos.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthService.UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserAlreadyExistsException(AuthService.UserAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AuthService.AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthException(AuthService.AuthException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DocumentService.UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(DocumentService.UserNotFound ex) {
        return ex.getMessage();
    }
}

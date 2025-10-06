package edu.udea.sigepos.exception;

import edu.udea.sigepos.service.AuthService;
import edu.udea.sigepos.service.DocumentService;
import edu.udea.sigepos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserService.UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserAlreadyExistsException(UserService.UserAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AuthService.AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthException(AuthService.AuthException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DocumentService.UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(DocumentService.UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserService.UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserService.UserNotFoundException ex) {
        return ex.getMessage();
    }
}

package ray1024.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ray1024.userservice.exception.UserAlreadyExistsException;
import ray1024.userservice.exception.UserNotFoundException;
import ray1024.userservice.model.response.ErrorResponse;

import java.time.Instant;
import java.util.Arrays;

@ControllerAdvice
@ResponseBody
public class ExceptionsHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.ofNullable(
                ErrorResponse.builder()
                        .message("User already exists")
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.ofNullable(
                ray1024.userservice.model.response.ErrorResponse.builder()
                        .message("Unknown user")
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception e) {
        return ResponseEntity.ofNullable(
                ray1024.userservice.model.response.ErrorResponse.builder()
                        .message(Arrays.toString(e.getStackTrace()))
                        .timestamp(Instant.now())
                        .build()
        );
    }
}

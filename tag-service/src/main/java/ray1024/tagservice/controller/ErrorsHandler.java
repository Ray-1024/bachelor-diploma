package ray1024.tagservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ray1024.tagservice.model.response.ErrorResponse;

import java.time.Instant;

@ControllerAdvice
public class ErrorsHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleOtherExceptions(final Exception ex) {
        return ResponseEntity.ofNullable(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .timestamp(Instant.now())
                        .build()
        );
    }
}

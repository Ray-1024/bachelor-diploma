package ray1024.imageservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ray1024.articleservice.exception.ArticleAlreadyExistsException;
import ray1024.articleservice.exception.ArticleNotFoundException;
import ray1024.articleservice.exception.ImageSavingException;
import ray1024.articleservice.exception.WrongAuthorException;
import ray1024.articleservice.model.response.ErrorResponse;

import java.time.Instant;

@ControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ErrorResponse> handleArticleNotFound(final ArticleNotFoundException ex) {
        return ResponseEntity.ofNullable(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(ArticleAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleArticleAlreadyExists(final ArticleAlreadyExistsException ex) {
        return ResponseEntity.ofNullable(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(ImageSavingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleImageSaving(final ImageSavingException ex) {
        return ResponseEntity.ofNullable(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(WrongAuthorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleWrongAuthor(final WrongAuthorException ex) {
        return ResponseEntity.ofNullable(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .timestamp(Instant.now())
                        .build()
        );
    }

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

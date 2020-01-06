package de.th.bingen.master.backend.controller;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.DockerClientBuilder;
import de.th.bingen.master.backend.model.response.ErrorMessage;
import org.apache.http.ConnectionClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.ws.rs.ProcessingException;
import java.lang.reflect.Field;
import java.net.ConnectException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleError(HttpMessageNotReadableException ex) {
        if (ex.getMessage().contains("Required request body is missing")) {
            return getErrorMessage("Required request body is missing", HttpStatus.BAD_REQUEST);
        } else {
            return getErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleError(MethodArgumentNotValidException ex) {
        FieldError f = ex.getBindingResult().getFieldError();
        return getErrorMessage("Field malformated or missing: " + f.getField(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleError(NotFoundException ex) {
        return getErrorMessage(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InterruptedException.class)
    public ResponseEntity<ErrorMessage> handleError(InterruptedException ex) {
        return getErrorMessage(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DockerClientException.class)
    public ResponseEntity<ErrorMessage> handleError(DockerClientException ex) {
        return getErrorMessage(ex.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<ErrorMessage> handleError(InternalServerErrorException ex) {
        return getErrorMessage(ex.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = ProcessingException.class)
    public ResponseEntity<ErrorMessage> handleError(ProcessingException ex) {
        return getErrorMessage(ex.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleError(RuntimeException ex) {
        return getErrorMessage(ex.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<ErrorMessage> getErrorMessage(String description, HttpStatus status) {
        return new ResponseEntity<>(new ErrorMessage(status,description),status);
    }
}

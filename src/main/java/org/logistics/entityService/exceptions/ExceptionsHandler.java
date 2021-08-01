package org.logistics.entityService.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.model.response.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(ex.getLocalizedMessage());
        returnValue.setError("INTERNAL SERVER ERROR");
        returnValue.setStatusCode(500);
        ex.printStackTrace();
        return new ResponseEntity<>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(ex.getLocalizedMessage());
        returnValue.setError("BAD REQUEST");
        returnValue.setStatusCode(400);
        return new ResponseEntity<>(returnValue, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(ex.getLocalizedMessage());
        returnValue.setError("BAD REQUEST");
        returnValue.setStatusCode(400);
        return new ResponseEntity<>(returnValue, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(ex.getMessage());
        returnValue.setError("BAD REQUEST");
        returnValue.setStatusCode(400);
        return new ResponseEntity<>(returnValue, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(EntityServiceException ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(ex.getMessage());
        returnValue.setError("BAD REQUEST");
        returnValue.setStatusCode(400);
        return new ResponseEntity<>(returnValue, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(processFieldError(ex.getBindingResult().getFieldErrors()));
        returnValue.setError("BAD REQUEST");
        returnValue.setStatusCode(400);
        return new ResponseEntity<>(returnValue, HttpStatus.BAD_REQUEST);
    }

    private List<String> processFieldError(List<FieldError> errorList) {
        List<String> returnList = new ArrayList<>();
        for (FieldError fieldError : errorList) {
            returnList.add(fieldError.getDefaultMessage());
        }
        return returnList;
    }
}

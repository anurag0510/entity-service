package org.logistics.entityService.exceptions;

import org.logistics.entityService.model.response.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
        ErrorMessage returnValue = new ErrorMessage();
        returnValue.setDetails(ex.getLocalizedMessage());
        returnValue.setError("INTERNAL SERVER ERROR");
        returnValue.setStatusCode(500);
        return new ResponseEntity<>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
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

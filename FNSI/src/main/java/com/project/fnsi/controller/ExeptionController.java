package com.project.fnsi.controller;

import com.project.fnsi.exeptions.CreatingMappingException;
import com.project.fnsi.exeptions.AppExeptionData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExeptionController {
    @ExceptionHandler
    public ResponseEntity<AppExeptionData> handleException(CreatingMappingException exception) {
        AppExeptionData data = new AppExeptionData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<AppExeptionData> handleException(Exception exception) {
        AppExeptionData data = new AppExeptionData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler
    public ResponseEntity<AppExeptionData> handleException(EntityNotFoundException exception) {
        AppExeptionData data = new AppExeptionData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}

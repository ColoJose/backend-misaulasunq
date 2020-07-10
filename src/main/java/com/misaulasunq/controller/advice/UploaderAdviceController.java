package com.misaulasunq.controller.advice;

import com.misaulasunq.controller.api.UploaderController;
import com.misaulasunq.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.io.IOException;

@RestControllerAdvice(assignableTypes = UploaderController.class)
public class UploaderAdviceController extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = {InvalidCellFormatException.class, DegreeNotFoundException.class, NoDataHeaderException.class, IOException.class, InconsistentRowException.class, ClassroomNotFoundException.class, InvalidFileExtensionException.class, NoSheetFoundException.class})
    public ResponseEntity<String> handleRuntimeException(Exception exception){
        LOGGER.error("We Get an unhandle excpetion! \n {}\n {}\n {}", exception.getClass(), exception.getMessage(), exception.getCause());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}

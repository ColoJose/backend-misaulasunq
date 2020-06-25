package com.misaulasunq.controller.advice;

import com.misaulasunq.controller.api.SubjectController;
import com.misaulasunq.controller.api.UploaderController;
import com.misaulasunq.exceptions.*;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice(assignableTypes = UploaderController.class)
public class UploaderAdviceController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidCellFormatException.class, DegreeNotFoundException.class, NoDataHeaderException.class, IOException.class, InconsistentRowException.class, ClassroomNotFoundException.class, InvalidFileExtensionException.class, NoSheetFoundException.class})
    public ResponseEntity<String> handleRuntimeException(Exception exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}

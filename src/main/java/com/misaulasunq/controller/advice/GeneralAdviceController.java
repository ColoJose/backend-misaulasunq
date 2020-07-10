package com.misaulasunq.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class GeneralAdviceController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception){
        LOGGER.error("We Get an unhandle excpetion! \n {}\n {}\n {}", exception.getClass(), exception.getMessage(), exception.getCause());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String response = exception.getMessage();

        // OBS: cambio provisorio para poder handlear error de codigo de materia repetido
        if(this.errorFromNewSubjectMethod(exception)){
            response = "Clave Duplicada";
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).body(response);
    }

    private boolean errorFromNewSubjectMethod(Exception anException){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        anException.printStackTrace(pw);

        return anException.getClass().getSimpleName().equalsIgnoreCase("DataIntegrityViolationException")
            && ((DataIntegrityViolationException) anException).getRootCause().getMessage().contains("Duplicate entry")
            && sw.toString().contains("createNewSubject");
    }
}

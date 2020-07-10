package com.misaulasunq.controller.advice;

import com.misaulasunq.controller.api.SubjectController;
import com.misaulasunq.exceptions.SubjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = SubjectController.class)
public class SubjectAdviceController extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({SubjectNotFoundException.class})
    public ResponseEntity<String> handleEventNotFoundException(SubjectNotFoundException exception) {
        LOGGER.error("We Get an unhandle excpetion! \n {}\n {}\n {}", exception.getClass(), exception.getMessage(), exception.getCause());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    // handle @Valid errors
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();

        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map( x -> x.getDefaultMessage())
                                .collect(Collectors.toList());

        body.put("errors",errors);

        return new ResponseEntity<>(body, headers, status);
    }

}

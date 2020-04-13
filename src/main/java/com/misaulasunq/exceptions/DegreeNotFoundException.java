package com.misaulasunq.exceptions;

public class DegreeNotFoundException extends RuntimeException {

    public DegreeNotFoundException(Integer id) {
        super(String.format("Degree with id: %s not found",id.toString()));
    }
}

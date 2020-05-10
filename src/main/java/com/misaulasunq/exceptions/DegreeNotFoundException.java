package com.misaulasunq.exceptions;

public class DegreeNotFoundException extends Exception {

    public DegreeNotFoundException(Integer id) {
        super(String.format("Degree with id: %s not found",id.toString()));
    }
}

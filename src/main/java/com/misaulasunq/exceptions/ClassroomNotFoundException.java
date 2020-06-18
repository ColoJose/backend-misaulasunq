package com.misaulasunq.exceptions;

public class ClassroomNotFoundException extends Exception {

    public ClassroomNotFoundException(Integer id) {
        super(String.format("Classroom with id: %s not found", id.toString()));
    }

}

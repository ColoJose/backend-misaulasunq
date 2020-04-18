package com.misaulasunq.exceptions;

public class SubjectNotfoundException extends RuntimeException {

    public SubjectNotfoundException(Integer id){
        super(String.format("Subject with the id: %s not found", id.toString()));
    }

    public SubjectNotfoundException(String classroomnumber){
        super(String.format("No subjects in the classroom %s.", classroomnumber));
    }
}

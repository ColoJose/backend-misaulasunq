package com.misaulasunq.exceptions;

public class SubjectNotfoundException extends RuntimeException {

    public static SubjectNotfoundException SubjectNotFoundByNumber(String classroomnumber){
        return new SubjectNotfoundException(String.format("No subjects in the classroom %s.", classroomnumber));
    }

    public static SubjectNotfoundException SubjectNotFoundByName(String name){
        return new SubjectNotfoundException(String.format("No subjects with the Name %s.", name));
    }

    public SubjectNotfoundException(Integer id){
        super(String.format("Subject with the id: %s not found", id.toString()));
    }

    public SubjectNotfoundException(String message){
        super(message);
    }
}

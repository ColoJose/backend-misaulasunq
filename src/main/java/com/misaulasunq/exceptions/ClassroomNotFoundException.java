package com.misaulasunq.exceptions;

public class ClassroomNotFoundException extends Exception {

    public ClassroomNotFoundException(Integer id) {
        super(String.format("Classroom with id: %s not found", id.toString()));
    }

    public ClassroomNotFoundException(String classroomNumber) {
        super(
            String.format(
                    "No se Encuentra el aula %s. Corrija el código o pida el alta de la misma.",
                    classroomNumber
            )
        );
    }
}

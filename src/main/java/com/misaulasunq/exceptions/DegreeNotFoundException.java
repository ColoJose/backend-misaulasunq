package com.misaulasunq.exceptions;

public class DegreeNotFoundException extends Exception {

    public DegreeNotFoundException(Integer id) {
        super(String.format("Degree with id: %s not found",id.toString()));
    }

    public DegreeNotFoundException(String degreeCode) {
        super(
            String.format(
                "No se Encuentra Carrera con el Código %S. Corrija el código o pida el alta de la misma.",
                degreeCode
            )
        );
    }
}

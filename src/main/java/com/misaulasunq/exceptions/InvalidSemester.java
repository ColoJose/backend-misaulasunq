package com.misaulasunq.exceptions;

public class InvalidSemester extends Exception {

    public InvalidSemester(String aString) {
        super(
            String.format(
                "No se puede convertir el semestre seleccionado. "
                +"Los valores permitidos son \"Primer\",\"Segundo\",\"Anual\" y se recibió %s",
                aString
            )
        );
    }
}

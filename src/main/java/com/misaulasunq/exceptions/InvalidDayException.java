package com.misaulasunq.exceptions;

public class InvalidDayException extends Exception {

    public InvalidDayException(String aString) {
        super(
            String.format(
                "No se puede convertir el Día. "
                + "Los valores permitidos son \"Lunes\",\"Martes\",\"Miercoles\","
                + "\"Miércoles\",\"Jueves\",\"Viernes\",\"Sabado\",\"Sábado\" y se recibió %s",
                aString
            )
        );
    }
}

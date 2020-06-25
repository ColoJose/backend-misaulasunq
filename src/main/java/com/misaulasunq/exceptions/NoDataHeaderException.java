package com.misaulasunq.exceptions;

public class NoDataHeaderException extends Exception {

    public NoDataHeaderException() {
        super(
            "No se encuentran los encabezados de las columnas. Por favor, revise el archivo."
        );
    }
}

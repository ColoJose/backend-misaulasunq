package com.misaulasunq.exceptions;

public class NoSheetFoundException extends Throwable {

    public NoSheetFoundException(String sheetName) {
        super(
            String.format("Hoja con el Nombre %s No Encontrada.", sheetName)
        );
    }
}

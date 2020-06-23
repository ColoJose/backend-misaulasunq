package com.misaulasunq.exceptions;

import org.apache.poi.ss.usermodel.CellType;

public class InvalidCellFormatException extends Exception{

    public InvalidCellFormatException(CellType cellType, String expectedFormat, Integer rowPosition, String columnName) {
        super(
            String.format(
                    "Formato de Celda Invalido en Fila %s Columna %s. "
                    +"Si hay celdas combinadas, no deben estarlo."
                    +"Se espera en formato %s y se recibio en formato %s",
                    rowPosition+1,
                    columnName,
                    expectedFormat,
                    cellType
            ));
    }

    public InvalidCellFormatException(CellType cellType, String valueReceived, String expectedFormat, Integer rowPosition, String columnName) {
        super(
                String.format(
                        "Formato o Valor de Celda Invalido en Fila %s Columna %s. "
                        +"Si hay celdas combinadas, no deben estarlo."
                        +"Se espera en formato %s y se recibio en formato %s con le valor %s",
                        rowPosition+1,
                        columnName,
                        expectedFormat,
                        cellType,
                        valueReceived
                ));
    }
}

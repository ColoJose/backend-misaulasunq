package com.misaulasunq.exceptions;

import org.apache.poi.ss.usermodel.CellType;

public class InvalidCellFormat extends Exception{

    public InvalidCellFormat(CellType cellType, String expectedFormat, Integer rowPosition, String columnName) {
        super(
            String.format(
                    "Formato de Celda Invalido en Fila %s Columna %s. "
                    +"Si hay celdas combinadas, no deben estarlo."
                    +"Se espera en formato %s y se recibio en formato %s",
                    rowPosition,
                    columnName,
                    expectedFormat,
                    cellType
            ));
    }

    public InvalidCellFormat(CellType cellType, String valueReceived, String expectedFormat, Integer rowPosition, String columnName) {
        super(
                String.format(
                        "Formato o Valor de Celda Invalido en Fila %s Columna %s. "
                        +"Si hay celdas combinadas, no deben estarlo."
                        +"Se espera en formato %s y se recibio en formato %s con le valor %s",
                        rowPosition,
                        columnName,
                        expectedFormat,
                        cellType,
                        valueReceived
                ));
    }
}

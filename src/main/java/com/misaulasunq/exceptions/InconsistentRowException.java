package com.misaulasunq.exceptions;

public class InconsistentRowException extends Exception {
    public InconsistentRowException(int row, int cellToProcess) {
        super(
            String.format(
                "Ocurrío un error al intentar procesar la celda %s de la fila %S. "
                    +"Revise que sea una fila con datos a importar y que no tenga celda "
                    +"con espacios en blanco o datos innecesarios.",
                cellToProcess+1,
                row+1
            )
        );
    }
}

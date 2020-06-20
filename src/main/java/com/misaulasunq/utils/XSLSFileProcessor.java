package com.misaulasunq.utils;

import com.misaulasunq.exceptions.InvalidCellFormat;
import com.misaulasunq.exceptions.InvalidDay;
import com.misaulasunq.exceptions.InvalidSemester;
import com.misaulasunq.model.*;
import com.misaulasunq.utils.fileProcessor.RowFileWrapper;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

// TODO: estaria bueno hacer un cell processor y un row processor para separar las responsabilidades
public class XSLSFileProcessor {

    private final List<String> validFormats = List.of(".xslx", ".xsl", ".csv");
    private final Pair<String, Integer> CODIGO_CARRERA = new MutablePair<>("Código Carrera", 0);
    private final Pair<String, Integer> NOMBRE_MATERIA = new MutablePair<>("Nombre Materia", 0);
    private final Pair<String, Integer> CODIGO_MATERIA = new MutablePair<>("Código Materia", 0);
    private final Pair<String, Integer> NOMBRE_COMISION = new MutablePair<>("Nombre Comisión", 0);
    private final Pair<String, Integer> SEMESTRE = new MutablePair<>("Semestre", 0);
    private final Pair<String, Integer> HORA_INICIO = new MutablePair<>("Hora de Inicio", 0);
    private final Pair<String, Integer> HORA_FIN = new MutablePair<>("Hora de Fin", 0);
    private final Pair<String, Integer> DIA = new MutablePair<>("Dia", 0);
    private final Pair<String, Integer> AULA = new MutablePair<>("Aula", 0);

    private Set<String> degreeCodes;
    private Set<String> classroomNumbers;
    private Map<String, List<RowFileWrapper>> rowsWrapperBySubjectCode;

    public XSLSFileProcessor() {
        this.initialize();
    }

    private void initialize() {
        this.degreeCodes = new HashSet<>();
        this.classroomNumbers = new HashSet<>();
        this.rowsWrapperBySubjectCode = new HashMap<>();
    }

    /**
     * Se asume que se recibe un sheet con los headers y al menos una celda para importar
     */
    public void processFile(Sheet worksheet) throws InvalidCellFormat {
        //itero sobre la primera fila paraquedarme con la posicion de cada dato a impotar
        Iterator<Row> rowIterator = worksheet.iterator();
        this.recordDataPositions(rowIterator);

        //procesamos todas las filas armando los objetos y mapeandolos
        Row rowToProcess;
        List<RowFileWrapper> rowsMapped;
        RowFileWrapper rowFileWrapper;
        while (rowIterator.hasNext()) {
            rowToProcess = rowIterator.next();

            String subjectCode = this.getCellValueAsString(
                    rowToProcess.getCell(this.CODIGO_MATERIA.getRight()),
                    this.CODIGO_CARRERA
            );

            // Si ya hay una lista mapeada la traigo, sino la creo
            if(this.rowsWrapperBySubjectCode.containsKey(subjectCode)){
                rowsMapped = this.rowsWrapperBySubjectCode.get(subjectCode);
            } else {
                rowsMapped = new ArrayList<>();
            }
            rowFileWrapper = this.createRowWrapper(subjectCode,rowToProcess);
            this.degreeCodes.add(rowFileWrapper.getDegreeCode());
            this.classroomNumbers.add(rowFileWrapper.getClassroom());
            rowsMapped.add(rowFileWrapper);
            this.rowsWrapperBySubjectCode.put(subjectCode,rowsMapped);
        }
    }

    private RowFileWrapper createRowWrapper(String subjectCode, Row rowToProcess) throws InvalidCellFormat {
        String degreeCode = this.getCellValueAsString(
                rowToProcess.getCell(this.CODIGO_CARRERA.getRight()),
                this.CODIGO_CARRERA
        );
        String subjectName = this.getCellValueAsString(
                rowToProcess.getCell(this.NOMBRE_MATERIA.getRight()),
                this.NOMBRE_MATERIA
        );
        String commissionName = this.getCellValueAsString(
                rowToProcess.getCell(this.NOMBRE_COMISION.getRight()),
                this.NOMBRE_COMISION
        );
        Semester semester = this.getCellValueAsSemesterEnum(
                rowToProcess.getCell(this.SEMESTRE.getRight()),
                this.SEMESTRE
        );
        LocalTime startTime = this.getCellValueAsLocalTime(
                rowToProcess.getCell(this.HORA_INICIO.getRight()),
                this.HORA_INICIO
        );
        LocalTime endTime = this.getCellValueAsLocalTime(
                rowToProcess.getCell(this.HORA_FIN.getRight()),
                this.HORA_FIN
        );
        Day day = this.getCellValueAsDayEnum(
                rowToProcess.getCell(this.DIA.getRight()),
                this.DIA
        );
        String classroomNumber = this.getCellValueAsString(
                rowToProcess.getCell(this.AULA.getRight()),
                this.AULA
        );
        return new RowFileWrapper(
                degreeCode,
                subjectName,
                subjectCode,
                commissionName,
                semester,
                LocalDate.now().getYear(),
                startTime,
                endTime,
                day,
                classroomNumber
        );
    }

    private Day getCellValueAsDayEnum(Cell cell, Pair<String, Integer> aPair) throws InvalidCellFormat {
        Day dayToReturn = null;

        boolean hasErrorOrInvalidType = !cell.getCellType().equals(CellType.STRING);

        try{
            if (! hasErrorOrInvalidType) {
                dayToReturn = DayConverter.stringToDay(cell.getStringCellValue());
            }
        } catch (InvalidDay invalidFormat){
            hasErrorOrInvalidType = true;
        }

        if(hasErrorOrInvalidType){
            throw new InvalidCellFormat(
                    cell.getCellType(),
                    cell.toString(),
                    "Texto entre \"Lunes\",\"Martes\",\"Miercoles\","
                            + "\"Miércoles\",\"Jueves\",\"Viernes\",\"Sabado\" o \"Sábado\"",
                    cell.getRowIndex(),
                    aPair.getLeft()
            );
        }

        return dayToReturn;
    }

    private LocalTime getCellValueAsLocalTime(Cell cell, Pair<String, Integer> aPair) throws InvalidCellFormat {
        LocalTime timeToReturn;
        boolean hasErrorOrInvalidType = !cell.getCellType().equals(CellType.NUMERIC);

        if(! hasErrorOrInvalidType) {
            timeToReturn = cell.getLocalDateTimeCellValue().toLocalTime();
        } else {
            throw new InvalidCellFormat(
                        cell.getCellType(),
                        cell.toString(),
                        "Hora (Ej: 20:00)",
                        cell.getRowIndex(),
                        aPair.getLeft());
        }
        return timeToReturn;
    }

    private Semester getCellValueAsSemesterEnum(Cell cell, Pair<String, Integer> aPair) throws InvalidCellFormat {
        Semester semesterToReturn = null;
        boolean hasErrorOrInvalidType = !cell.getCellType().equals(CellType.STRING);

        try{
            if (! hasErrorOrInvalidType) {
                semesterToReturn = SemesterConverter.stringToSemester(cell.getStringCellValue());
            }
        } catch (InvalidSemester invalidFormat){
            hasErrorOrInvalidType = true;
        }

        if(hasErrorOrInvalidType){
            throw new InvalidCellFormat(
                    cell.getCellType(),
                    cell.toString(),
                    "Texto entre Primer, Segundo o Anual",
                    cell.getRowIndex(),
                    aPair.getLeft()
            );
        }

        return semesterToReturn;
    }

    private String getCellValueAsString(Cell cell,Pair<String, Integer> aPair) throws InvalidCellFormat {
        String valueToReturn;
        switch (cell.getCellType()){
            case NUMERIC:
                valueToReturn = String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue());
                break;
            case STRING:
                valueToReturn = cell.getStringCellValue();
                break;
            default:
                throw new InvalidCellFormat(
                    cell.getCellType(),
                    "Número o Texto",
                    cell.getRowIndex(),
                    aPair.getLeft()
                );
        }
        return valueToReturn;
    }

    void recordDataPositions(Iterator<Row> rowIterator) {
        Row row = rowIterator.next();
        Cell cellToProcess;
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            cellToProcess = cellIterator.next();
            if(this.cellValueAreEqualTo(cellToProcess, CODIGO_CARRERA)) {
                CODIGO_CARRERA.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, NOMBRE_MATERIA)){
                NOMBRE_MATERIA.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, CODIGO_MATERIA)) {
                CODIGO_MATERIA.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, NOMBRE_COMISION)) {
                NOMBRE_COMISION.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, SEMESTRE)) {
                SEMESTRE.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, HORA_INICIO)) {
                HORA_INICIO.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, HORA_FIN)) {
                HORA_FIN.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, DIA)) {
                DIA.setValue(cellToProcess.getColumnIndex());
            } else if(this.cellValueAreEqualTo(cellToProcess, AULA)) {
                AULA.setValue(cellToProcess.getColumnIndex());
            }
        }
    }

    private boolean cellValueAreEqualTo(Cell cellToProcess, Pair<String, Integer> aPair){
        return cellToProcess.toString().equalsIgnoreCase(aPair.getKey());
    }

    public boolean isColumnsHeaderTheFirstRow(Sheet worksheet) {
        List<String> cellHeader = new ArrayList<>();
        Iterator<Cell> cellIterator = worksheet.getRow(worksheet.getFirstRowNum()).cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            cellHeader.add(cell.toString().toUpperCase());
        }
        return cellHeader.containsAll(this.getHeadersNamesAsList());
    }

    private List<String> getHeadersNamesAsList(){
        return List.of(
                this.CODIGO_CARRERA.getLeft().toUpperCase(),
                this.NOMBRE_MATERIA.getLeft().toUpperCase(),
                this.CODIGO_MATERIA.getLeft().toUpperCase(),
                this.NOMBRE_COMISION.getLeft().toUpperCase(),
                this.SEMESTRE.getLeft().toUpperCase(),
                this.HORA_INICIO.getLeft().toUpperCase(),
                this.HORA_FIN.getLeft().toUpperCase(),
                this.DIA.getLeft().toUpperCase(),
                this.AULA.getLeft().toUpperCase()
        );
    }

    public Boolean isValidFileExtension(String originalFilename) {
        return this.validFormats
                .stream()
                .reduce(false,
                        (Boolean partialResult, String validFormat) -> {
                                return partialResult
                                       || this.isValidFormat(originalFilename,validFormat);
                        },
                        Boolean::logicalOr);
    }

    private Boolean isValidFormat(String fileNameWithExtension, String formatToCompare){
        return fileNameWithExtension.toUpperCase().contains(formatToCompare.toUpperCase());
    }

    Pair<String, Integer> getCODIGO_CARRERA() {  return this.CODIGO_CARRERA; }
    Pair<String, Integer> getNOMBRE_MATERIA() { return this.NOMBRE_MATERIA;  }
    Pair<String, Integer> getCODIGO_MATERIA() {  return this.CODIGO_MATERIA;  }
    Pair<String, Integer> getNOMBRE_COMISION() { return this.NOMBRE_COMISION; }
    Pair<String, Integer> getSEMESTRE() {    return this.SEMESTRE;    }
    Pair<String, Integer> getHORA_INICIO() { return this.HORA_INICIO; }
    Pair<String, Integer> getHORA_FIN() {   return this.HORA_FIN;    }
    Pair<String, Integer> getDIA() {    return this.DIA; }
    Pair<String, Integer> getAULA() {    return this.AULA;    }

    public List<String> getSubjectsCodes() {
        return new ArrayList<>(this.getRowsWrapperBySubjectCode().keySet());
    }
    public Set<String> getDegreeCodes() {   return this.degreeCodes;    }
    public Set<String> getClassroomNumbers() {  return this.classroomNumbers;   }
    public Map<String, List<RowFileWrapper>> getRowsWrapperBySubjectCode() {    return rowsWrapperBySubjectCode;    }
}

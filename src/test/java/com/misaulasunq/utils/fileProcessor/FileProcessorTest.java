package com.misaulasunq.utils.fileProcessor;

import com.misaulasunq.exceptions.InconsistentRowException;
import com.misaulasunq.exceptions.InvalidCellFormatException;
import com.misaulasunq.model.*;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileProcessorTest {

    private ExcelFileProcessor aFileProcessor;

    @Before
    public void SetUp(){
        this.aFileProcessor = new ExcelFileProcessor();
    }

    @Test
    public void ifProcessAExcelWithOneRowAndAllDataItsOk_CreateAllObjectsToImport() throws IOException, InvalidCellFormatException, InconsistentRowException {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");

        //Exercise(When)
        this.aFileProcessor.processFile(worksheet);

        //Test(Then)
        assertEquals(1,this.aFileProcessor.getDegreeCodes().size());
        assertEquals(4,this.aFileProcessor.getClassroomNumbers().size());

        List<RowFileWrapper> mateIIRowWrapper = this.aFileProcessor.getRowsWrapperBySubjectCode().get("223");
        assertEquals(3,mateIIRowWrapper.size());
        assertEquals("102",mateIIRowWrapper.get(0).getDegreeCode());
        assertEquals("Matematica II",mateIIRowWrapper.get(0).getSubjectName());
        assertEquals("223",mateIIRowWrapper.get(0).getSubjectCode());
        assertEquals("Comision 1",mateIIRowWrapper.get(0).getCommissionName());
        assertEquals(Semester.PRIMER,mateIIRowWrapper.get(0).getSemster());
        assertEquals(LocalTime.of(12,0),mateIIRowWrapper.get(0).getStartTime());
        assertEquals(LocalTime.of(15,0),mateIIRowWrapper.get(0).getEndTime());
        assertEquals(Day.LUNES,mateIIRowWrapper.get(0).getDay());
        assertEquals(2020,mateIIRowWrapper.get(0).getYear());
        assertEquals("52",mateIIRowWrapper.get(0).getClassroom());

        assertEquals(3,mateIIRowWrapper.size());
        assertEquals("102",mateIIRowWrapper.get(1).getDegreeCode());
        assertEquals("Matematica II",mateIIRowWrapper.get(1).getSubjectName());
        assertEquals("223",mateIIRowWrapper.get(1).getSubjectCode());
        assertEquals("Comision 1",mateIIRowWrapper.get(1).getCommissionName());
        assertEquals(Semester.PRIMER,mateIIRowWrapper.get(1).getSemster());
        assertEquals(LocalTime.of(10,0),mateIIRowWrapper.get(1).getStartTime());
        assertEquals(LocalTime.of(13,0),mateIIRowWrapper.get(1).getEndTime());
        assertEquals(Day.JUEVES,mateIIRowWrapper.get(1).getDay());
        assertEquals(2020,mateIIRowWrapper.get(1).getYear());
        assertEquals("54",mateIIRowWrapper.get(1).getClassroom());

        assertEquals(3,mateIIRowWrapper.size());
        assertEquals("102",mateIIRowWrapper.get(2).getDegreeCode());
        assertEquals("Matematica II",mateIIRowWrapper.get(2).getSubjectName());
        assertEquals("223",mateIIRowWrapper.get(2).getSubjectCode());
        assertEquals("Comision 2",mateIIRowWrapper.get(2).getCommissionName());
        assertEquals(Semester.PRIMER,mateIIRowWrapper.get(2).getSemster());
        assertEquals(LocalTime.of(17,0),mateIIRowWrapper.get(2).getStartTime());
        assertEquals(LocalTime.of(20,0),mateIIRowWrapper.get(2).getEndTime());
        assertEquals(Day.SABADO,mateIIRowWrapper.get(2).getDay());
        assertEquals(2020,mateIIRowWrapper.get(2).getYear());
        assertEquals("36",mateIIRowWrapper.get(2).getClassroom());

        List<RowFileWrapper> bdRowWrapper = this.aFileProcessor.getRowsWrapperBySubjectCode().get("200");
        assertEquals(1,bdRowWrapper.size());
        assertEquals("102",bdRowWrapper.get(0).getDegreeCode());
        assertEquals("Base De Datos",bdRowWrapper.get(0).getSubjectName());
        assertEquals("200",bdRowWrapper.get(0).getSubjectCode());
        assertEquals("Comision 1",bdRowWrapper.get(0).getCommissionName());
        assertEquals(Semester.PRIMER,bdRowWrapper.get(0).getSemster());
        assertEquals(LocalTime.of(7,0),bdRowWrapper.get(0).getStartTime());
        assertEquals(LocalTime.of(10,0),bdRowWrapper.get(0).getEndTime());
        assertEquals(Day.MIERCOLES,bdRowWrapper.get(0).getDay());
        assertEquals(2020,bdRowWrapper.get(0).getYear());
        assertEquals("214",bdRowWrapper.get(0).getClassroom());
    }

    @Test(expected = InvalidCellFormatException.class)
    public void ifProcessAExcelWithACellWithOutExpectedFormat_GetAnInvalidFormatException() throws IOException, InvalidCellFormatException, InconsistentRowException {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample with bad format cell.xlsx");

        //Exercise(When)
        this.aFileProcessor.processFile(worksheet);
    }

    @Test
    public void ifTheExcelFileToProcessHasTheIncorrectHeaders_getsTrue() throws IOException {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Without Name Columns Test.xlsx");

        //Exercise(When)
        boolean hasCorrectheaders = this.aFileProcessor.isColumnsHeaderTheFirstRow(worksheet);

        //Test(Then)
        assertFalse(hasCorrectheaders, "No tiene todos los headers correctos, tiene que fallar");
    }

    @Test
    public void ifTheExcelFileToProcessHasTheCorrectHeaders_getsTrue() throws IOException {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test.xlsx");

        //Exercise(When)
        boolean hasCorrectheaders = this.aFileProcessor.isColumnsHeaderTheFirstRow(worksheet);

        //Test(Then)
        assertTrue(hasCorrectheaders, "Tiene los headers correctos, no puede fallar");
    }

    @Test
    public void ifTryToGetCellValueAsEnum_GetTheValue() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        cell.setCellValue("Lunes");

        //Exercise(When)
        Day day = this.aFileProcessor.getCellValueAsDayEnum(cell, new MutablePair<>());

        //Test(Then)
        assertEquals(Day.LUNES, day);
    }

    @Test(expected = InvalidCellFormatException.class)
    public void ifTryToGetCellValueAsDayEnumAndHaveWrongText_ThrowAnError() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        cell.setCellValue("Test");

        //Exercise(When)
        this.aFileProcessor.getCellValueAsDayEnum(cell, new MutablePair<>());
        //Test(Then)
    }

    @Test(expected = InvalidCellFormatException.class)
    public void ifTryToGetCellValueAsDayEnumAndNotItsStringType_ThrowAnError() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        Cell cell   = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue(Float.parseFloat("2.1"));

        //Exercise(When)
        this.aFileProcessor.getCellValueAsDayEnum(cell, new MutablePair<>());
        //Test(Then)
    }

    @Test(expected = InvalidCellFormatException.class)
    public void ifTryToGetCellValueAsLocalDateAndNotItsNumericType_ThrowAnError() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        cell.setCellValue("7:00");

        //Exercise(When)
        this.aFileProcessor.getCellValueAsDayEnum(cell, new MutablePair<>());
        //Test(Then)
    }

    @Test
    public void ifTryToGetCellValueAsLocalDate_GetValue() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("[HH]:MM"));
        Cell cell   = row.createCell(1);

        cell.setCellValue(12.5);

        //Exercise(When)
        LocalTime hour = this.aFileProcessor.getCellValueAsLocalTime(cell, new MutablePair<>());

        //Test(Then)
        assertEquals(LocalTime.of(12,0), hour);
    }

    @Test(expected = InvalidCellFormatException.class)
    public void ifTryToGetCellValueAsSemesterEnumAndHaveWrongText_ThrowAnError() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        cell.setCellValue("Cuarto");

        //Exercise(When)
        this.aFileProcessor.getCellValueAsSemesterEnum(cell, new MutablePair<>());
        //Test(Then)
    }

    @Test
    public void ifTryToGetCellValueAsSemesterEnum_GetValue() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        cell.setCellValue("Anual");

        //Exercise(When)
        Semester semester = this.aFileProcessor.getCellValueAsSemesterEnum(cell, new MutablePair<>());

        //Test(Then)
        assertEquals(Semester.ANUAL, semester);
    }

    @Test
    public void ifTryToGetCellValueAsString_GetValue() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        cell.setCellValue("153");
        Cell cell2   = row.createCell(1);
        cell2.setCellValue(22);

        //Exercise(When)
        String value1 = this.aFileProcessor.getCellValueAsString(cell, new MutablePair<>());
        String value2 = this.aFileProcessor.getCellValueAsString(cell2, new MutablePair<>());

        //Test(Then)
        assertEquals("153", value1);
        assertEquals("22", value2);
    }

    @Test(expected = InvalidCellFormatException.class)
    public void ifTryToGetCellValueAsStringAndNotIsCellTypeStringOrNumeric_ThrowAnError() throws InvalidCellFormatException {
        //Setup(Given)
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet");
        Row row     = sheet.createRow(1);
        Cell cell   = row.createCell(1);
        //Exercise(When)
        String value1 = this.aFileProcessor.getCellValueAsString(cell, new MutablePair<>());
    }

    private InputStream getInputStreamOf(String fileName){
        return ExcelFileProcessor.class
                .getClassLoader()
                .getResourceAsStream(fileName);
    }

    private Sheet getSheetOfExcelFile(String filename) throws IOException {
        return new XSSFWorkbook(this.getInputStreamOf(filename)).getSheetAt(0);
    }
}
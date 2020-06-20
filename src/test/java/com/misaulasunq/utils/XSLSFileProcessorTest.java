package com.misaulasunq.utils;

import com.misaulasunq.exceptions.InvalidCellFormat;
import com.misaulasunq.model.*;
import com.misaulasunq.utils.fileProcessor.RowFileWrapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XSLSFileProcessorTest {

    private XSLSFileProcessor aXslsFileProcessor;

    @Before
    public void SetUp(){
        this.aXslsFileProcessor = new XSLSFileProcessor();
    }

    @Test
    public void ifProcessAExcelWithOneRowAndAllDataItsOk_CreateAllObjectsToImport() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");

        //Exercise(When)
        this.aXslsFileProcessor.processFile(worksheet);

        //Test(Then)
        assertEquals(1,this.aXslsFileProcessor.getDegreeCodes().size());
        assertEquals(4,this.aXslsFileProcessor.getClassroomNumbers().size());

        List<RowFileWrapper> mateIIRowWrapper = this.aXslsFileProcessor.getRowsWrapperBySubjectCode().get("223");
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

        List<RowFileWrapper> bdRowWrapper = this.aXslsFileProcessor.getRowsWrapperBySubjectCode().get("200");
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

    @Test(expected = InvalidCellFormat.class)
    public void ifProcessAExcelWithACellWithOutExpectedFormat_GetAnInvalidFormatException() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample with bad format cell.xlsx");

        //Exercise(When)
        this.aXslsFileProcessor.processFile(worksheet);
    }

    @Test
    public void ifTheExcelFileToProcessHasTheIncorrectHeaders_getsTrue() throws IOException {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Without Name Columns Test.xlsx");

        //Exercise(When)
        boolean hasCorrectheaders = this.aXslsFileProcessor.isColumnsHeaderTheFirstRow(worksheet);

        //Test(Then)
        assertFalse(hasCorrectheaders, "No tiene todos los headers correctos, tiene que fallar");
    }

    @Test
    public void ifTheExcelFileToProcessHasTheCorrectHeaders_getsTrue() throws IOException {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test.xlsx");

        //Exercise(When)
        boolean hasCorrectheaders = this.aXslsFileProcessor.isColumnsHeaderTheFirstRow(worksheet);

        //Test(Then)
        assertTrue(hasCorrectheaders, "Tiene los headers correctos, no puede fallar");
    }

    @Test
    public void ifTheFilesToProcessAreNotOfAValidExtension_ReturnFalse(){
        //Setup(Given)

        //Exercise(When)
        Boolean formatExeIsValid = this.aXslsFileProcessor.isValidFileExtension("A Exe File.exe");
        Boolean formatDllIsValid = this.aXslsFileProcessor.isValidFileExtension("A Dll File.dll");

        //Test(Then)
        assertFalse(formatExeIsValid,"No tiene que validar! Es un archivo exe y es no formato valido!");
        assertFalse(formatDllIsValid,"No tiene que validar! Es un archivo dll y es no formato valido!");
    }

    @Test
    public void ifTheFilesToProcessAreOfAValidExtension_ReturnTrue(){
        //Exercise(When)
        Boolean isFormatXslx = aXslsFileProcessor.isValidFileExtension("A XSLX File.xslx");
        Boolean isFormatXsl = aXslsFileProcessor.isValidFileExtension("A XSL File.xsl");
        Boolean isFormatCsv = aXslsFileProcessor.isValidFileExtension("A CSV File.csv");

        //Test(Then)
        assertTrue(isFormatXslx,"Tiene que validar! Es un Xsls y es formato valido!");
        assertTrue(isFormatXsl,"Tiene que validar! Es un Xsl y es formato valido!");
        assertTrue(isFormatCsv,"Tiene que validar! Es un csv y es formato valido!");
    }

    private InputStream getInputStreamOf(String fileName){
        return XSLSFileProcessor.class
                .getClassLoader()
                .getResourceAsStream(fileName);
    }

    private Sheet getSheetOfExcelFile(String filename) throws IOException {
        return new XSSFWorkbook(this.getInputStreamOf(filename)).getSheetAt(0);
    }
}
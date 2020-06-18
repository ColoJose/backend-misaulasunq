package com.misaulasunq.utils;

import com.misaulasunq.exceptions.InvalidCellFormat;
import com.misaulasunq.model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XSLSFileProcessorTest {

    private XSLSFileProcessor aXslsFileProcessor;

    @Before
    public void SetUp(){
        this.aXslsFileProcessor = new XSLSFileProcessor();
    }

    @Test
    public void whenMakeAScheduleOfARow_ItsCreatedCorrectlyAndAssociatedToAClassroomAndComission() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Commission aComission = CommissionBuilder.buildACommission().build();
        Classroom aClassroom = ClassroomBuilder.buildAClassroom().build();
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");
        Row aRowToProcess = worksheet.getRow(worksheet.getFirstRowNum()+1);
        this.aXslsFileProcessor.getHORA_INICIO().setValue(5);
        this.aXslsFileProcessor.getHORA_FIN().setValue(6);
        this.aXslsFileProcessor.getDIA().setValue(7);

        //Exersice(When)
        this.aXslsFileProcessor.makeSchedule(aRowToProcess, aComission, aClassroom);

        //Test(Then)
        Schedule scheduleCreated = aComission.getSchedules().get(0);
        assertEquals(LocalTime.of(12,0), scheduleCreated.getStartTime(), "No se importo bien la hora de inicio");
        assertEquals(LocalTime.of(15,0), scheduleCreated.getEndTime(), "No se importo bien la hora de fin");
        assertEquals(Day.LUNES, scheduleCreated.getDay(), "No se importo bien el dia");
    }

    @Test
    public void whenMakeAClassroomOfARow_ItsCreatedCorrectly() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Classroom aClassroomToImport;
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");
        Row aRowToProcess = worksheet.getRow(worksheet.getFirstRowNum()+1);
        this.aXslsFileProcessor.getAULA().setValue(8);

        //Exersice(When)
        aClassroomToImport = this.aXslsFileProcessor.makeOrGetClassroom(aRowToProcess);

        //Test(Then)
        assertEquals("52", aClassroomToImport.getNumber(), "No se importo bien el numero de la comision");
    }

    @Test
    public void whenMakeACommisionOfARow_ItsCreatedCorrectlyAndAssociatedToASubject() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Commission aComissionToImport;
        Subject aSubject = SubjectBuilder.buildASubject().build();
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");
        Row aRowToProcess = worksheet.getRow(worksheet.getFirstRowNum()+1);
        this.aXslsFileProcessor.getNOMBRE_COMISION().setValue(3);
        this.aXslsFileProcessor.getSEMESTRE().setValue(4);

        //Exersice(When)
        aComissionToImport = this.aXslsFileProcessor.makeOrGetComission(aRowToProcess, aSubject);

        //Test(Then)
        assertEquals("Comision 1", aComissionToImport.getName(), "No se importo bien el nombre de la comision");
        assertEquals(LocalDate.now().getYear(), aComissionToImport.getYear(), "No se importo bien el año de la comision");
        assertEquals(Semester.PRIMER, aComissionToImport.getSemester(), "No se seteo correctamente el semestre");
        assertNotNull(aComissionToImport.getSubject(), "Se tuvo que haber asignado la materia a la comision");
        assertEquals(aSubject, aComissionToImport.getSubject(), "No es la comision que se tiene que asignar");
    }

    @Test
    public void whenMakeASubjetOfARow_ItsCreatedCorrectly() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Degree aDegree = DegreeBuilder.buildADegree().withDegreeCode("223").build();
        Subject aSubjectToimport;
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");
        Row aRowToProcess = worksheet.getRow(worksheet.getFirstRowNum()+1);
        this.aXslsFileProcessor.getNOMBRE_MATERIA().setValue(1);
        this.aXslsFileProcessor.getCODIGO_MATERIA().setValue(2);

        //Exersice(When)
        aSubjectToimport = this.aXslsFileProcessor.makeOrGetSubject(aRowToProcess, aDegree);

        //Test(Then)
        assertEquals("Matematica II", aSubjectToimport.getName(), "No se importo bien el nombre");
        assertEquals("223", aSubjectToimport.getSubjectCode(), "No se importo bien el codigo");
        assertEquals(1, aSubjectToimport.getDegrees().size(), "Solo tiene que haber 1 carrera asociada a la materia");
        assertTrue(aSubjectToimport.getDegrees().contains(aDegree), "La carrera tiene que estar relacionada con la materia");
    }

    @Test
    public void whenMakeADegreeOfARow_ItsCreatedCorrectly() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Degree aDegreeToimport;
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");
        Row aRowToProcess = worksheet.getRow(worksheet.getFirstRowNum()+1);
        this.aXslsFileProcessor.getCODIGO_CARRERA().setValue(0);

        //Exersice(When)
        aDegreeToimport = this.aXslsFileProcessor.makeOrGetDegree(aRowToProcess);

        //Test(Then)
        assertEquals(1, this.aXslsFileProcessor.getDegreeByCode().size(), "Solo tiene que haber 1 carrera mapeada");
        assertEquals("102", aDegreeToimport.getCode(), "No se parseo bien el codigo de la materia");
    }

    @Test
    public void ifProcessAExcelWithOneRowAndAllDataItsOk_CreateAllObjectsToImport() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Sheet worksheet = this.getSheetOfExcelFile("Import Sample Test 2.xlsx");

        //Exercise(When)
        this.aXslsFileProcessor.processFile(worksheet);

        //Test(Then)
        assertEquals(1,this.aXslsFileProcessor.getDegreeByCode().size());
        assertEquals(2,this.aXslsFileProcessor.getSubjesctsByCode().size());
        assertEquals(4,this.aXslsFileProcessor.getClassroomByNumber().size());

        Classroom aula52 = this.aXslsFileProcessor.getClassroomByNumber().get("52");
        assertEquals("52",aula52.getNumber());
        assertEquals(1,aula52.getSchedules().size());

        Classroom aula54 = this.aXslsFileProcessor.getClassroomByNumber().get("54");
        assertEquals("54",aula54.getNumber());
        assertEquals(1,aula54.getSchedules().size());

        Classroom aula36 = this.aXslsFileProcessor.getClassroomByNumber().get("36");
        assertEquals("36",aula36.getNumber());
        assertEquals(1,aula36.getSchedules().size());

        Classroom aula214= this.aXslsFileProcessor.getClassroomByNumber().get("214");
        assertEquals("214",aula214.getNumber());
        assertEquals(1,aula214.getSchedules().size());

        Degree aDegreeToimport = this.aXslsFileProcessor.getDegreeByCode().get("102");
        assertEquals("102",aDegreeToimport.getCode());
        assertEquals(2,aDegreeToimport.getSubjects().size());

        Subject matematicaII = this.aXslsFileProcessor.getSubjesctsByCode().get("223");
        assertEquals("Matematica II", matematicaII.getName());
        assertEquals("223", matematicaII.getSubjectCode());
        assertEquals(1, matematicaII.getDegrees().size());
        assertEquals(2, matematicaII.getCommissions().size());

        Commission matematicaIIComisionI = matematicaII.getCommissions().get(0);
        assertEquals("Comision 1", matematicaIIComisionI.getName());
        assertEquals(Semester.PRIMER, matematicaIIComisionI.getSemester());
        assertEquals(2, matematicaIIComisionI.getSchedules().size());
        assertEquals(matematicaII, matematicaIIComisionI.getSubject());

        Schedule mateIIC1ScheduleLunes = matematicaIIComisionI.getSchedules().get(0);
        assertEquals(LocalTime.of(12,0), mateIIC1ScheduleLunes.getStartTime());
        assertEquals(LocalTime.of(15,0), mateIIC1ScheduleLunes.getEndTime());
        assertEquals(Day.LUNES, mateIIC1ScheduleLunes.getDay());
        assertEquals(matematicaIIComisionI, mateIIC1ScheduleLunes.getCommission());
        assertEquals(aula52, mateIIC1ScheduleLunes.getClassroom());

        Schedule mateIIC1ScheduleJueves = matematicaIIComisionI.getSchedules().get(1);
        assertEquals(LocalTime.of(10,0), mateIIC1ScheduleJueves.getStartTime());
        assertEquals(LocalTime.of(13,0), mateIIC1ScheduleJueves.getEndTime());
        assertEquals(Day.JUEVES, mateIIC1ScheduleJueves.getDay());
        assertEquals(matematicaIIComisionI, mateIIC1ScheduleJueves.getCommission());
        assertEquals(aula54, mateIIC1ScheduleJueves.getClassroom());

        Commission matematicaIIComisionII = matematicaII.getCommissions().get(1);
        assertEquals("Comision 2", matematicaIIComisionII.getName());
        assertEquals(Semester.PRIMER, matematicaIIComisionII.getSemester());
        assertEquals(1, matematicaIIComisionII.getSchedules().size());
        assertEquals(matematicaII, matematicaIIComisionII.getSubject());

        Schedule mateIIC2ScheduleSabado = matematicaIIComisionII.getSchedules().get(0);
        assertEquals(LocalTime.of(17,0), mateIIC2ScheduleSabado.getStartTime());
        assertEquals(LocalTime.of(20,0), mateIIC2ScheduleSabado.getEndTime());
        assertEquals(Day.SABADO, mateIIC2ScheduleSabado.getDay());
        assertEquals(matematicaIIComisionII, mateIIC2ScheduleSabado.getCommission());
        assertEquals(aula36, mateIIC2ScheduleSabado.getClassroom());

        Subject baseDeDatos = this.aXslsFileProcessor.getSubjesctsByCode().get("200");
        assertEquals("Base De Datos", baseDeDatos.getName());
        assertEquals("200", baseDeDatos.getSubjectCode());
        assertEquals(1, baseDeDatos.getDegrees().size());
        assertEquals(1, baseDeDatos.getCommissions().size());

        Commission baseDeDatosComisionI = baseDeDatos.getCommissions().get(0);
        assertEquals("Comision 1", baseDeDatosComisionI.getName());
        assertEquals(Semester.PRIMER, baseDeDatosComisionI.getSemester());
        assertEquals(1, baseDeDatosComisionI.getSchedules().size());
        assertEquals(baseDeDatos, baseDeDatosComisionI.getSubject());

        Schedule bdCIScheduleMiercoles = baseDeDatosComisionI.getSchedules().get(0);
        assertEquals(LocalTime.of(7,0), bdCIScheduleMiercoles.getStartTime());
        assertEquals(LocalTime.of(10,0), bdCIScheduleMiercoles.getEndTime());
        assertEquals(Day.MIERCOLES, bdCIScheduleMiercoles.getDay());
        assertEquals(baseDeDatosComisionI, bdCIScheduleMiercoles.getCommission());
        assertEquals(aula214, bdCIScheduleMiercoles.getClassroom());
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
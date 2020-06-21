package com.misaulasunq.service;

import com.misaulasunq.TestConfig;
import com.misaulasunq.exceptions.*;
import com.misaulasunq.model.*;
import com.misaulasunq.persistance.*;
import com.misaulasunq.utils.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Rollback
@Transactional
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {TestConfig.class})
public class UploaderServiceTest {

    @Autowired private UploaderService uploaderService;
    @Autowired private DegreeRepository degreeRepository;
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private CommissionRepository commissionRepository;
    @Autowired private ScheduleRepository scheduleRepository;

    @Test
    public void ifProcessAMultipartOfANewExcelFileWithOutError_AlObjectsAreImportedCorrectly() throws IOException, InvalidCellFormatException, InconsistentRowException, ClassroomNotFoundException, NoDataHeaderException, DegreeNotFoundException, InvalidFileExtensionException, NoSheetFoundException {
        //Setup(Given)
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().withDegreeCode("102").build();
        degreeRepository.save(aDegree);
        Classroom aClassroom = ClassroomBuilder.buildAClassroom().withName("52").build();
        classroomRepository.save(aClassroom);

        MultipartFile fileXslxToValidate =
            new MockMultipartFile(
                "Import Sample Test",
                "Import Sample Test.xlsx",
                String.valueOf(MediaType.MULTIPART_FORM_DATA),
                this.getClass().getClassLoader().getResourceAsStream("Import Sample Test.xlsx")
            );

        //Exercise(When)
        this.uploaderService.processSubjectHoursFile(fileXslxToValidate);

        //Test(Test)
        assertEquals(1,degreeRepository.count(),"Tiene que haber solo una carrera insertada");
        assertEquals(1,classroomRepository.count(),"Tiene que haber solo un aula insertada");
        assertEquals(1,subjectRepository.count(), "Tiene que haberse importado una materia");
        assertEquals(1,commissionRepository.count(), "Tiene que haberse importao una comision");
        assertEquals(1,scheduleRepository.count(), "Tiene que haberse importado un horario");
    }

    @Test(expected = NoDataHeaderException.class)
    public void ifTryToImportAFileWithOutHeaders_GetAnException() throws IOException, InconsistentRowException, InvalidCellFormatException, NoDataHeaderException, DegreeNotFoundException, ClassroomNotFoundException, InvalidFileExtensionException, NoSheetFoundException {
        //Setup(Given)
        MultipartFile fileXslxToValidate =
                new MockMultipartFile(
                        "Import Sample Without Name Columns Test",
                        "Import Sample Without Name Columns Test.xlsx",
                        String.valueOf(MediaType.MULTIPART_FORM_DATA),
                        this.getClass().getClassLoader().getResourceAsStream("Import Sample Without Name Columns Test.xlsx")
                );

        //Exercise(When)
        this.uploaderService.processSubjectHoursFile(fileXslxToValidate);
    }

    @Test
    public void ifTheFilesToProcessAreNotOfAValidExtension_ReturnFalse(){
        //Setup(Given)

        //Exercise(When)
        Boolean formatExeIsValid = this.uploaderService.isValidFileExtension("A Exe File.exe");
        Boolean formatDllIsValid = this.uploaderService.isValidFileExtension("A Dll File.dll");
        Boolean formatCsvIsInvalid = this.uploaderService.isValidFileExtension("A CSV File.csv");

        //Test(Then)
        assertFalse(formatExeIsValid,"No tiene que validar! Es un archivo exe y no es formato valido!");
        assertFalse(formatDllIsValid,"No tiene que validar! Es un archivo dll y no es formato valido!");
        assertFalse(formatCsvIsInvalid,"No tiene que validar! Es un csv y no es formato valido!");
    }

    @Test
    public void ifTheFilesToProcessAreOfAValidExtension_ReturnTrue(){
        //Exercise(When)
        Boolean isFormatXslx = this.uploaderService.isValidFileExtension("A XSLX File.xlsx");
        Boolean isFormatXsl = this.uploaderService.isValidFileExtension("A XSL File.xls");

        //Test(Then)
        assertTrue(isFormatXslx,"Tiene que validar! Es un Xsls y es formato valido!");
        assertTrue(isFormatXsl,"Tiene que validar! Es un Xsl y es formato valido!");
    }

    @Test
    public void ifProcessAMultipartOfAOldExcelFileWithOutError_AlObjectsAreImportedCorrectly() throws IOException, InvalidCellFormatException, InconsistentRowException, ClassroomNotFoundException, NoDataHeaderException, DegreeNotFoundException, InvalidFileExtensionException, NoSheetFoundException {
        //Setup(Given)
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().withDegreeCode("102").build();
        Classroom aClassroom = ClassroomBuilder.buildAClassroom().withName("52").build();
        Subject funcional = SubjectBuilder.buildASubject()
                .withName("Programacion Funcional")
                .withSubjectCode("26")
                .withDegrees(new ArrayList<>(List.of(aDegree)))
                .build();
        aDegree.addSubject(funcional);
        Schedule funcSchedule = ScheduleBuilder.buildASchedule()
                .withMockData()
                .withClassroom(aClassroom)
                .withStartTime(LocalTime.of(10,0))
                .withEndTime(LocalTime.of(12,0))
                .build();
        aClassroom.addSchedule(funcSchedule);
        Commission funcC1 = CommissionBuilder.buildACommission()
                .withMockData()
                .withName("Comision 1")
                .withSchedules(new ArrayList<>(List.of(funcSchedule)))
                .withSubject(funcional)
                .build();
        funcional.addCommission(funcC1);
        funcSchedule.setCommission(funcC1);
        degreeRepository.save(aDegree);

        MultipartFile fileXslxToValidate =
                new MockMultipartFile(
                        "Old Excel Import Sample",
                        "Old Excel Import Sample.xls",
                        String.valueOf(MediaType.MULTIPART_FORM_DATA),
                        this.getClass().getClassLoader().getResourceAsStream("Old Excel Import Sample.xls")
                );

        //Exercise(When)
        this.uploaderService.processSubjectHoursFile(fileXslxToValidate);

        //Test(Test)
        assertEquals(1,degreeRepository.count(),"Tiene que haber solo una carrera insertada");
        assertEquals(1,classroomRepository.count(),"Tiene que haber solo un aula insertada");
        assertEquals(2,subjectRepository.count(), "Tiene que haberse importado una materia");
        assertEquals(3,commissionRepository.count(), "Tiene que haberse importao una comision");
        assertEquals(3,scheduleRepository.count(), "Tiene que haberse importado un horario");
    }

}
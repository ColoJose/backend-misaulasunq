package com.misaulasunq.service;

import com.misaulasunq.TestConfig;
import com.misaulasunq.exceptions.InvalidCellFormat;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Degree;
import com.misaulasunq.persistance.*;
import com.misaulasunq.utils.ClassroomBuilder;
import com.misaulasunq.utils.DegreeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {TestConfig.class})
public class UploaderServiceTest{

    @Autowired private UploaderService uploaderService;
    @Autowired private DegreeRepository degreeRepository;
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private CommissionRepository commissionRepository;
    @Autowired private ScheduleRepository scheduleRepository;

    //recibe el archivo multipart
    //chequea que extension es (Sino excepcion)
    //chequea que tenga los headers correctos (Sino excepcion)
    //procesa el archivo
    @Test
    public void ifProcessAMultipartNewExcelFileWithOutError_AlObjectsAreImportedCorrectly() throws IOException, InvalidCellFormat {
        //Setup(Given)
        Degree aDegree = DegreeBuilder.buildADegree().withMockData().withDegreeCode("102").build();
        degreeRepository.save(aDegree);
        Classroom aClassroom = ClassroomBuilder.buildAClassroom().withName("52").build();
        classroomRepository.save(aClassroom);

        MultipartFile fileXslxToValidate =
            new MockMultipartFile(
                "Import Sample Test",
                "Import Sample Test.xslx",
                String.valueOf(MediaType.MULTIPART_FORM_DATA),
                this.getClass().getClassLoader().getResourceAsStream("Import Sample Test.xlsx")
            );

        //Exercise(When)
        uploaderService.processSubjectHoursFile(fileXslxToValidate);

        //Test(Test)
        assertEquals(1,degreeRepository.count(),"Tiene que haber solo una carrera insertada");
        assertEquals(1,classroomRepository.count(),"Tiene que haber solo un aula insertada");
        assertEquals(1,subjectRepository.count(), "Tiene que haberse importado una materia");
        assertEquals(1,commissionRepository.count(), "Tiene que haberse importao una comision");
        assertEquals(1,scheduleRepository.count(), "Tiene que haberse importado un horario");
    }
}
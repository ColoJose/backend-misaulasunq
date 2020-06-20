package com.misaulasunq.utils.fileProcessor;

import com.misaulasunq.exceptions.ClassroomNotFound;
import com.misaulasunq.exceptions.DegreeNotFoundException;
import com.misaulasunq.model.*;
import com.misaulasunq.persistance.ClassroomRepository;
import com.misaulasunq.persistance.DegreeRepository;
import com.misaulasunq.persistance.SubjectRepository;
import com.misaulasunq.utils.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

@Rollback
@DataJpaTest
@Transactional
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoadProcessorTest {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private DegreeRepository degreeRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    private LoadProcessor loadProcessorSUT;

    @Before
    public void setUp() {
        // Classrooms
        Classroom aula522 = ClassroomBuilder.buildAClassroom().withName("522").build();
        Classroom aula20  = ClassroomBuilder.buildAClassroom().withName("20").build();
        //Degree
        Degree aDegree  = DegreeBuilder.buildADegree().withMockData().withDegreeCode("22").build();
        Degree aDegree2 = DegreeBuilder.buildADegree().withMockData().withDegreeCode("32").build();
        //Subjects
        Subject desap = SubjectBuilder.buildASubject()
                .withName("Desarrollo de Aplicaciones")
                .withSubjectCode("33")
                .withDegrees(new ArrayList<>(List.of(aDegree)))
                .build();
        aDegree.addSubject(desap);
        // Schedules
        Schedule desapSchedule = ScheduleBuilder.buildASchedule().withMockData()
                .withClassroom(aula522)
                .withStartTime(LocalTime.of(1,30))
                .withEndTime(LocalTime.of(4,30))
                .build();
        aula522.addSchedule(desapSchedule);
        // Commissions
        Commission desapC1 = CommissionBuilder.buildACommission().withMockData()
                .withSchedules(new ArrayList<>(List.of(desapSchedule)))
                .withSubject(desap)
                .build();
        desap.addCommission(desapC1);
        desapSchedule.setCommission(desapC1);

        subjectRepository.save(desap);
        degreeRepository.save(aDegree2);
        classroomRepository.save(aula20);
    }

    @Test
    public void makeRelationships() throws DegreeNotFoundException, ClassroomNotFound {
        //Setup(Given)
        Map<String, Degree> degreesInDb     = this.getDbDegreesMapped();
        Map<String, Subject> subjectsInDb   = this.getDbSubjectsMapped();
        Map<String, Classroom> classroomInDb= this.getDbClassroomsMapped();
        Map<String, List<RowFileWrapper>> rowsToProcess = new HashMap<>();

        RowFileWrapper row = new RowFileWrapper(
                                        "22",
                                        "Base De Datos",
                                        "153",
                                        "Comision 1",
                                        Semester.PRIMER,
                                        2019,
                                        LocalTime.of(10,0),
                                        LocalTime.of(12,0),
                                        Day.JUEVES,
                                        "20");
        RowFileWrapper row1 = new RowFileWrapper(
                                        "22",
                                        "Desarrollo de Aplicaciones",
                                        "33",
                                        "Comision 2",
                                        Semester.PRIMER,
                                        2019,
                                        LocalTime.of(8,0),
                                        LocalTime.of(13,0),
                                        Day.LUNES,
                                        "522");
        rowsToProcess.put("153", Arrays.asList(row));
        rowsToProcess.put("33", Arrays.asList(row1));

        this.loadProcessorSUT = new LoadProcessor(
                degreesInDb,
                subjectsInDb,
                classroomInDb,
                rowsToProcess
        );

        //Exercise(When)
        this.loadProcessorSUT.makeRelationships();

        //Test(Then)
        //Subject subjectToImport = this.loadProcessorSUT.get("33");
        //Degree subjectDegree = subjectToImport.getDegrees().get(0);
        //Schedule subejctSchedule = subjectToImport.getCommissions().get(0).getSchedules().get(0);
        //assertEquals(degreesInDb.get("22").getId(), subjectDegree.getId());
        //assertEquals(classroomInDb.get("522").getId(), subejctSchedule.getClassroom().getId());
    }

    private Map<String, Classroom> getDbClassroomsMapped() {
        Map<String, Classroom> classroomsMap = new HashMap<>();
        for(Classroom eachClassroom : this.classroomRepository.findAll()){
            classroomsMap.put(eachClassroom.getNumber(),eachClassroom);
        }
        return classroomsMap;
    }

    private Map<String, Subject> getDbSubjectsMapped() {
        Map<String, Subject> subjectMap = new HashMap<>();
        for(Subject eachSubject : this.subjectRepository.findAll()){
            subjectMap.put(eachSubject.getSubjectCode(),eachSubject);
        }
        return subjectMap;
    }

    private Map<String, Degree> getDbDegreesMapped() {
        Map<String, Degree> degreesMap = new HashMap<>();
        for(Degree eachDegree : this.degreeRepository.findAll()){
            degreesMap.put(eachDegree.getCode(),eachDegree);
        }
        return degreesMap;
    }
}
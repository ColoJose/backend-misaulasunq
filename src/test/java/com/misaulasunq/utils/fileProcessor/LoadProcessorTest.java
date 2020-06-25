package com.misaulasunq.utils.fileProcessor;

import com.misaulasunq.exceptions.ClassroomNotFoundException;
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
import static org.junit.Assert.*;

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
        Degree aDegree  = DegreeBuilder.buildADegree().withName("Carrera 22").withDegreeCode("22").build();
        Degree aDegree2 = DegreeBuilder.buildADegree().withName("Carrera 32").withDegreeCode("32").build();
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

    @Test(expected = ClassroomNotFoundException.class)
    public void whenTryToMakeRelationshipForImportedRowsThatHaveNonexistentClassroom_ThrowException() throws DegreeNotFoundException, ClassroomNotFoundException {
        //Setup(given)
        Map<String, Degree> degreesInDb     = this.getDbDegreesMapped();
        Map<String, Subject> subjectsInDb   = this.getDbSubjectsMapped();
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
        rowsToProcess.put("153", Arrays.asList(row));

        this.loadProcessorSUT = new LoadProcessor(
                degreesInDb,
                subjectsInDb,
                new HashMap<>(),
                rowsToProcess
        );

        //Exercise(When)
        this.loadProcessorSUT.makeRelationships();
    }

    @Test(expected = DegreeNotFoundException.class)
    public void whenTryToMakeRelationshipsForIMportedRowsThatHaveNonexistentDegree_ThrowException() throws DegreeNotFoundException, ClassroomNotFoundException {
        //Setup(given)
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
        rowsToProcess.put("153", Arrays.asList(row));

        this.loadProcessorSUT = new LoadProcessor(
                new HashMap<>(),
                subjectsInDb,
                classroomInDb,
                rowsToProcess
        );

        //Exercise(When)
        this.loadProcessorSUT.makeRelationships();
    }

    @Test
    public void whenMakeRelationshipsForImportedRowsIfAsSubjectsInDbAreLinkedAndIfNotAreCreated() throws DegreeNotFoundException, ClassroomNotFoundException {
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
                                        "Base De Datos",
                                        "153",
                                        "Comision 1",
                                        Semester.PRIMER,
                                        2019,
                                        LocalTime.of(12,0),
                                        LocalTime.of(16,0),
                                        Day.MARTES,
                                        "20");
        RowFileWrapper row2 = new RowFileWrapper(
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
        rowsToProcess.put("153", Arrays.asList(row, row1));
        rowsToProcess.put("33", Arrays.asList(row2));

        this.loadProcessorSUT = new LoadProcessor(
                degreesInDb,
                subjectsInDb,
                classroomInDb,
                rowsToProcess
        );

        //Exercise(When)
        this.loadProcessorSUT.makeRelationships();

        //Test(Then)
        assertEquals(2, this.loadProcessorSUT.getSubjectsToUpsert().size());

        Subject subjectDesarrollo = this.loadProcessorSUT.getSubjectsToUpsert().get(0);
        assertEquals(1, subjectDesarrollo.getDegrees().size());
        assertEquals("Carrera 22", subjectDesarrollo.getDegrees().get(0).getName());
        assertEquals("Desarrollo de Aplicaciones", subjectDesarrollo.getName());
        assertEquals("33", subjectDesarrollo.getSubjectCode());
        assertNotNull(subjectDesarrollo.getId());
        assertEquals(2, subjectDesarrollo.getCommissions().size());

        Commission desarrolloComission = subjectDesarrollo.getCommissions().get(0);
        assertEquals(subjectDesarrollo, desarrolloComission.getSubject());
        assertNotEquals("Comision 2", desarrolloComission.getName());

        Commission desarrolloComission2ToImport = subjectDesarrollo.getCommissions().get(1);
        assertEquals(subjectDesarrollo, desarrolloComission2ToImport.getSubject());
        assertEquals("Comision 2", desarrolloComission2ToImport.getName());
        assertEquals(Semester.PRIMER, desarrolloComission2ToImport.getSemester());
        assertEquals(2019, desarrolloComission2ToImport.getYear().intValue());
        assertEquals(1, desarrolloComission2ToImport.getSchedules().size());

        Schedule desarrolloC2Schedule = desarrolloComission2ToImport.getSchedules().get(0);
        assertEquals(desarrolloComission2ToImport, desarrolloC2Schedule.getCommission());
        assertEquals("522", desarrolloC2Schedule.getClassroom().getNumber());
        assertEquals(Day.LUNES, desarrolloC2Schedule.getDay());
        assertEquals(LocalTime.of(8,0), desarrolloC2Schedule.getStartTime());
        assertEquals(LocalTime.of(13,0), desarrolloC2Schedule.getEndTime());
        assertEquals(0, desarrolloC2Schedule.getNotices().size());

        Subject subjectBaseDeDatos = this.loadProcessorSUT.getSubjectsToUpsert().get(1);
        assertEquals(1, subjectBaseDeDatos.getDegrees().size());
        assertEquals("Carrera 22", subjectBaseDeDatos.getDegrees().get(0).getName());
        assertEquals("Base De Datos", subjectBaseDeDatos.getName());
        assertEquals("153", subjectBaseDeDatos.getSubjectCode());
        assertNull(subjectBaseDeDatos.getId());
        assertEquals(1, subjectBaseDeDatos.getCommissions().size());

        Commission baseDeDatosCommission = subjectBaseDeDatos.getCommissions().get(0);
        assertEquals(subjectBaseDeDatos, baseDeDatosCommission.getSubject());
        assertEquals("Comision 1", baseDeDatosCommission.getName());
        assertEquals(Semester.PRIMER, baseDeDatosCommission.getSemester());
        assertEquals(2019, baseDeDatosCommission.getYear().intValue());
        assertEquals(2, baseDeDatosCommission.getSchedules().size());

        Schedule baseDeDatosC1Schedule = baseDeDatosCommission.getSchedules().get(0);
        assertEquals(baseDeDatosCommission, baseDeDatosC1Schedule.getCommission());
        assertEquals("20", baseDeDatosC1Schedule.getClassroom().getNumber());
        assertEquals(Day.JUEVES, baseDeDatosC1Schedule.getDay());
        assertEquals(LocalTime.of(10,0), baseDeDatosC1Schedule.getStartTime());
        assertEquals(LocalTime.of(12,0), baseDeDatosC1Schedule.getEndTime());
        assertEquals(0, baseDeDatosC1Schedule.getNotices().size());

        Schedule baseDeDatosC1Schedule2 = baseDeDatosCommission.getSchedules().get(1);
        assertEquals(baseDeDatosCommission, baseDeDatosC1Schedule2.getCommission());
        assertEquals("20", baseDeDatosC1Schedule2.getClassroom().getNumber());
        assertEquals(Day.MARTES, baseDeDatosC1Schedule2.getDay());
        assertEquals(LocalTime.of(12,0), baseDeDatosC1Schedule2.getStartTime());
        assertEquals(LocalTime.of(16,0), baseDeDatosC1Schedule2.getEndTime());
        assertEquals(0, baseDeDatosC1Schedule2.getNotices().size());
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
package com.misaulasunq;

import com.misaulasunq.model.*;
import com.misaulasunq.persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/** Clase encargada de proporsionar datos de prueba para la aplicacion*/
@Component
public class BootstrapRunner implements ApplicationRunner {

    @Autowired
    private DegreeRepository degreeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.loadSampleData();
    }

    private void loadSampleData() {
        //Creacion de Carrera
        Degree tpi = new Degree();
        tpi.setName("Tecnicatura Universitaria en Programacion Informatica");

        Degree biotecnologia = new Degree();
        biotecnologia.setName("Lic. en Biotecnología");

        Degree automatizacion = new Degree();
        automatizacion.setName("Ingeniería en automatización");

        // Creacion de aulas
        Map<String, Classroom> classroomByNumber = new HashMap<>();
        Classroom room52 = new Classroom();
        room52.setNumber("52");
        room52.setImageClassRoomBase64("https://miro.medium.com/max/11344/1*32h8ts3A-7XNr6A4Js87ng.jpeg");
        classroomByNumber.put(room52.getNumber(),room52);

        Classroom roomCyT1 = new Classroom();
        roomCyT1.setNumber("CyT-1");
        roomCyT1.setImageClassRoomBase64("https://filedn.com/ltOdFv1aqz1YIFhf4gTY8D7/ingus-info/BLOGS/Photography-stocks3/stock-photography-slider.jpg");
        classroomByNumber.put(roomCyT1.getNumber(),roomCyT1);

        //Creacion de materias
        this.createMatematica(tpi, classroomByNumber);
        this.createSistemasOperativos(tpi, classroomByNumber);
        this.createProgramacionObjetos3(tpi, classroomByNumber);
        this.createSeguridadInformatica(tpi, classroomByNumber);
        this.createQuimicaOrganica(biotecnologia,classroomByNumber);
        this.createAnalisisI(automatizacion,classroomByNumber);
        this.createTIP(tpi,classroomByNumber);

        degreeRepository.save(tpi);
        degreeRepository.save(biotecnologia);
        degreeRepository.save(automatizacion);
    }

    private void createAnalisisI(Degree automatizacion, Map<String, Classroom> classroomByNumber) {
        Subject analisisI = this.createSubject(automatizacion, "Análisis matemático I", "105");
        Commission quimicaOrganicaC1Miercoles = this.createCommission(analisisI, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                quimicaOrganicaC1Miercoles,
                Day.MARTES,
                LocalTime.of(16,0),
                LocalTime.of(18,0)
        );
    }

    private void createQuimicaOrganica(Degree biotecnologia, Map<String, Classroom> classroomByNumber) {
        Subject quimicaOrganica = this.createSubject(biotecnologia, "Química Orgánica", "89");
        Commission quimicaOrganicaC1Miercoles = this.createCommission(quimicaOrganica, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                quimicaOrganicaC1Miercoles,
                Day.MIERCOLES,
                LocalTime.of(16,0),
                LocalTime.of(18,0)
        );
    }

    private void createProgramacionObjetos3(Degree aDegree, Map<String, Classroom> classroomByNumber) {
        Subject progObjectos3 = this.createSubject(aDegree, "Programacion Orientada a Objetos 3", "15");
        Commission progObjetos3C1Viernes = this.createCommission(progObjectos3, "Comision 1",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                progObjetos3C1Viernes,
                Day.VIERNES,
                LocalTime.of(16,0),
                LocalTime.of(22,0)
        );
    }

    public void createSistemasOperativos(Degree aDegree, Map<String,Classroom> classroomByNumber){
        Subject sistemasOperativos = this.createSubject(aDegree, "Sistemas Operativos", "150");
        Commission sistemasOpC1 = this.createCommission(sistemasOperativos, "Comision 1",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                sistemasOpC1,
                Day.LUNES,
                LocalTime.of(16,0),
                LocalTime.of(22,0)
        );
    }

    public void createSeguridadInformatica(Degree degree, Map<String, Classroom> classroomByNumber) {
        Subject seguridadInformatica = this.createSubject(degree, "Seguridad Informática", "420");
        Commission seguridadC1 = this.createCommission(seguridadInformatica,"Comision 1",2019,Semester.PRIMER);
        this.createSchedule(classroomByNumber.get("CyT-1"),
                seguridadC1,
                Day.SABADO,
                LocalTime.of(9,0),
                LocalTime.of(13,0)
        );
    }

    public void createTIP(Degree degree, Map<String, Classroom> clasroomByNumber) {
        Subject tip = this.createSubject(degree, "TIP", "231");
        Commission seguridadC1 = this.createCommission(tip,"Comision 1",2019,Semester.PRIMER);
        this.createSchedule(clasroomByNumber.get("52"),
                seguridadC1,
                Day.SABADO,
                LocalTime.of(8,0),
                LocalTime.of(13,0)
        );
    }

    public void createMatematica(Degree aDegree, Map<String,Classroom> classroomByNumber){
        Subject matematica1 = this.createSubject(aDegree,"Matematica I", "223");

        //COMISION 1
        Commission matematica1C1 = this.createCommission(matematica1, "Comision 1",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                matematica1C1,
                Day.MARTES,
                LocalTime.of(9,0),
                LocalTime.of(13,0)
            );
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                matematica1C1,
                Day.JUEVES,
                LocalTime.of(9,0),
                LocalTime.of(13,0)
            );

        //COMISION 2
        Commission matematica1C2 = this.createCommission(matematica1, "Comision 2",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("52"),
                matematica1C2,
                Day.LUNES,
                LocalTime.of(18,0),
                LocalTime.of(22,0)
        );
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                matematica1C2,
                Day.MIERCOLES,
                LocalTime.of(18,0),
                LocalTime.of(22,0)
            );

        //COMISION 3
        Commission matematica1C3 = this.createCommission(matematica1, "Comision 3",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("52"),
                matematica1C3,
                Day.LUNES,
                LocalTime.of(9,0),
                LocalTime.of(13,0)
            );
        this.createSchedule(
                classroomByNumber.get("52"),
                matematica1C3,
                Day.MIERCOLES,
                LocalTime.of(9,0),
                LocalTime.of(13,0)
            );
    }

    private Subject createSubject(Degree aDegree, String name, String code){
        Subject subject = new Subject();
        subject.setName(name);
        subject.setSubjectCode(code);
        subject.addDegree(aDegree);
        aDegree.addSubject(subject);
        return subject;
    }

    private Schedule createSchedule(Classroom aClassroom, Commission aCommission, Day aday, LocalTime startTime, LocalTime endTime){
        Schedule aSchudule = new Schedule();
        aSchudule.setDay(aday);
        aSchudule.setStartTime(startTime);
        aSchudule.setEndTime(endTime);
        aSchudule.setClassroom(aClassroom);
        aSchudule.setCommission(aCommission);
        aClassroom.addSchedule(aSchudule);
        aCommission.addSchedule(aSchudule);
        return aSchudule;
    }

    private Commission createCommission(Subject aSubject, String name, Integer aYear, Semester aSemester){
        Commission aCommission = new Commission();
        aCommission.setSemester(aSemester);
        aCommission.setYear(aYear);
        aCommission.setName(name);
        aCommission.setSubject(aSubject);
        aSubject.addCommission(aCommission);
        return aCommission;
    }
}

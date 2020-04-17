package com.misaulasunq;

import com.misaulasunq.model.*;
import com.misaulasunq.persistance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/** Clase encargada de proporsionar datos de prueba para la aplicacion*/
@Component
public class BootstrapRunner implements ApplicationRunner {

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.loadSampleData();
    }


    private void loadSampleData() {
        //Creacion de Carrera
        Degree tpi = new Degree();
        tpi.setName("Tecnicatura Universitaria en Programacion Informatica");

        // Creacion de aulas
        Classroom room52 = new Classroom();
        room52.setNumber("52");
        room52.setImageClassRoomBase64("https://miro.medium.com/max/11344/1*32h8ts3A-7XNr6A4Js87ng.jpeg");

        Classroom roomCyT1 = new Classroom();
        roomCyT1.setNumber("CyT-1");
        roomCyT1.setImageClassRoomBase64("https://filedn.com/ltOdFv1aqz1YIFhf4gTY8D7/ingus-info/BLOGS/Photography-stocks3/stock-photography-slider.jpg");

        //Creacion de materias
        Subject matematica1 = new Subject();
        matematica1.setName("Matematica I");
        matematica1.setSubjectCode("223");

        Schedule mateC1martes = new Schedule();
        mateC1martes.setDay(Day.MARTES);
        mateC1martes.setStartTime(LocalTime.of(9,0));
        mateC1martes.setEndTime(LocalTime.of(13,0));

        Schedule mateC1Jueves = new Schedule();
        mateC1Jueves.setDay(Day.JUEVES);
        mateC1Jueves.setStartTime(LocalTime.of(9,0));
        mateC1Jueves.setEndTime(LocalTime.of(13,0));

        Commission matematica1C1 = new Commission();
        matematica1C1.setName("Comision 1");

        Schedule mateC2Lunes = new Schedule();
        mateC2Lunes.setDay(Day.LUNES);
        mateC2Lunes.setStartTime(LocalTime.of(18,0));
        mateC2Lunes.setEndTime(LocalTime.of(22,0));

        Schedule mateC2Miercoles = new Schedule();
        mateC2Miercoles.setDay(Day.MIERCOLES);
        mateC2Miercoles.setStartTime(LocalTime.of(18,0));
        mateC2Miercoles.setEndTime(LocalTime.of(22,0));

        Commission matematica1C2 = new Commission();
        matematica1C2.setName("Comision 2");

        Schedule mateC3Lunes = new Schedule();
        mateC3Lunes.setDay(Day.LUNES);
        mateC3Lunes.setStartTime(LocalTime.of(9,0));
        mateC3Lunes.setEndTime(LocalTime.of(13,0));

        Schedule mateC3Miercoles = new Schedule();
        mateC3Miercoles.setDay(Day.MIERCOLES);
        mateC3Miercoles.setStartTime(LocalTime.of(9,0));
        mateC3Miercoles.setEndTime(LocalTime.of(13,0));

        Commission matematica1C3 = new Commission();
        matematica1C3.setName("Comision 3");

        //Creacion de materias 2
        Subject sistemasOperativos = new Subject();
        sistemasOperativos.setName("Sistemas Operativos");
        sistemasOperativos.setSubjectCode("150");

        Schedule soC1Lunes = new Schedule();
        soC1Lunes.setDay(Day.LUNES);
        soC1Lunes.setStartTime(LocalTime.of(16,0));
        soC1Lunes.setEndTime(LocalTime.of(22,0));

        Commission sistemasOpC1 = new Commission();
        sistemasOpC1.setName("Comision I");

        // Creaacion de materias 3
        Subject progObjectos3 = new Subject();
        progObjectos3.setName("Programacion Orientada a Objetos 3");
        progObjectos3.setSubjectCode("15");

        Schedule progObjetos3C1Viernes = new Schedule();
        progObjetos3C1Viernes.setDay(Day.VIERNES);
        progObjetos3C1Viernes.setStartTime(LocalTime.of(16,0));
        progObjetos3C1Viernes.setEndTime(LocalTime.of(22,0));

        Commission progObjetos3C1C1 = new Commission();
        progObjetos3C1C1.setName("Comision I");

        commissionRepository.saveAll(List.of(progObjetos3C1C1,sistemasOpC1,matematica1C3,matematica1C2,matematica1C1));
        scheduleRepository.saveAll(List.of(mateC2Lunes, mateC3Lunes, mateC3Miercoles,mateC1martes, mateC1Jueves, mateC2Miercoles));
        subjectRepository.saveAll(List.of(matematica1, sistemasOperativos,progObjectos3));
        degreeRepository.save(tpi);
        classroomRepository.saveAll(List.of(room52,roomCyT1));

        //Mapeo de comisiones en aulas
        progObjetos3C1Viernes.setClassroom(roomCyT1);
        soC1Lunes.setClassroom(roomCyT1);
        mateC3Lunes.setClassroom(roomCyT1);
        mateC3Miercoles.setClassroom(roomCyT1);
        mateC2Lunes.setClassroom(roomCyT1);
        mateC2Miercoles.setClassroom(room52);
        mateC1martes.setClassroom(room52);
        mateC1Jueves.setClassroom(room52);

        // Mapeo de horarios en comisiones
        progObjetos3C1C1.setSchedules(Set.of(progObjetos3C1Viernes));
        sistemasOpC1.setSchedules(Set.of(soC1Lunes));
        matematica1C3.setSchedules(Set.of(mateC3Lunes,mateC3Miercoles));
        matematica1C2.setSchedules(Set.of(mateC2Lunes,mateC2Miercoles));
        matematica1C1.setSchedules(Set.of(mateC1martes,mateC1Jueves));

        // Mapeo de materias en comisiones
        progObjetos3C1C1.setSubject(progObjectos3);
        sistemasOpC1.setSubject(sistemasOperativos);
        matematica1C3.setSubject(matematica1);
        matematica1C2.setSubject(matematica1);
        matematica1C1.setSubject(matematica1);

        //Mapeo de comisiones en horarios
        progObjetos3C1Viernes.setCommission(progObjetos3C1C1);
        soC1Lunes.setCommission(sistemasOpC1);
        mateC3Lunes.setCommission(matematica1C2);
        mateC3Miercoles.setCommission(matematica1C2);
        mateC2Lunes.setCommission(matematica1C2);
        mateC2Miercoles.setCommission(matematica1C2);
        mateC1martes.setCommission(matematica1C1);
        mateC1Jueves.setCommission(matematica1C1);

        //Mapeo de comisiones en aulass
        room52.setSchedules(List.of(mateC2Lunes, mateC3Lunes, mateC3Miercoles));
        roomCyT1.setSchedules(List.of(mateC1martes, mateC1Jueves, mateC2Miercoles));
        //Mapeo de materias en la carrera y carrera en las materias
        tpi.setSubjects(Set.of(matematica1, sistemasOperativos,progObjectos3));
        matematica1.setDegrees(Set.of(tpi));
        sistemasOperativos.setDegrees(Set.of(tpi));
        progObjectos3.setDegrees(Set.of(tpi));

        //Se actualizan las relaciones
        degreeRepository.save(tpi);
        commissionRepository.saveAll(List.of(progObjetos3C1C1,sistemasOpC1,matematica1C3,matematica1C2,matematica1C1));
    }
}

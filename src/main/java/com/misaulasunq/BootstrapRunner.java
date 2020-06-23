package com.misaulasunq;

import com.misaulasunq.model.*;
import com.misaulasunq.persistance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private OverlapNoticeRepository overlapNoticeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("BootstrapRunner Run");
        this.loadSampleData();
        LOGGER.info("BootstrapRunner End");
    }

    private void loadSampleData() {
        LOGGER.info("Creating and loading sample data");
        //Creacion de Carreras
        Degree tpi = new Degree();
        tpi.setCode("102");
        tpi.setName("Tecnicatura Universitaria en Programacion Informatica");

        Degree biotecnologia = new Degree();
        biotecnologia.setCode("103");
        biotecnologia.setName("Lic. en Biotecnología");

        Degree automatizacion = new Degree();
        automatizacion.setCode("104");
        automatizacion.setName("Ingeniería en automatización");

        Degree liceducacion = new Degree();
        liceducacion.setName("Licenciatura en educacion");

        Degree comercio = new Degree();
        comercio.setName("Licenciatura en Comercio Internacional");

        // Creacion de aulas
        Map<String, Classroom> classroomByNumber = new HashMap<>();
        Classroom room52 = new Classroom();
        room52.setNumber("52");
        classroomByNumber.put(room52.getNumber(),room52);

        Classroom roomCyT1 = new Classroom();
        roomCyT1.setNumber("CyT-1");
        classroomByNumber.put(roomCyT1.getNumber(),roomCyT1);

        //Creacion de materias

        // tpi
        this.createMatematica(tpi, classroomByNumber);
        this.createSistemasOperativos(tpi, classroomByNumber);
        this.createProgramacionObjetos3(tpi, classroomByNumber);
        this.createSeguridadInformatica(tpi, classroomByNumber);
        this.createTIP(tpi,classroomByNumber);
        Subject inglesII = this.createInglesII(tpi,classroomByNumber);
        // bio
        Subject qumicaOrganica = this.createQuimicaOrganica(biotecnologia,classroomByNumber);
        this.createBiologiaGeneral(biotecnologia,classroomByNumber);
        this.createMicrobioGeneral(biotecnologia,classroomByNumber);
        this.createBioquimica(biotecnologia,classroomByNumber);
        // automatizacion
        this.createAnalisisI(automatizacion,classroomByNumber);
        this.createAlgebraLineal(automatizacion,classroomByNumber);
        // educacion
        this.createHistoriaArgentina(liceducacion,classroomByNumber);
        this.createDidacticaYPedadogia(liceducacion,classroomByNumber);
        this.createPsicologia(liceducacion,classroomByNumber);
        // comercio
        this.createMicroEconomia(comercio,classroomByNumber);
        this.createMacroEconomia(comercio,classroomByNumber);
        this.introALaEconomia(comercio,classroomByNumber);
        LOGGER.info("Inserting sample data");

        degreeRepository.save(tpi);
        degreeRepository.save(biotecnologia);
        degreeRepository.save(automatizacion);

        //Creacion de nota de solapado para Qumica e Ingles II
        OverlapNotice aOverlapNotice = this.createScheduleOverlap(qumicaOrganica,inglesII);
        overlapNoticeRepository.save(aOverlapNotice);

        LOGGER.info("Sample data inserted");
        LOGGER.info("Sample data created and loaded");
    }

    private void introALaEconomia(Degree comercio, Map<String, Classroom> classroomByNumber) {
        Subject macro = this.createSubject(comercio, "Introducción a la Economía", "1999");
        Commission commission = this.createCommission(macro, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("52"),
                commission,
                Day.MIERCOLES,
                LocalTime.of(10,0),
                LocalTime.of(15,0)
        );
    }

    private void createMacroEconomia(Degree comercio, Map<String, Classroom> classroomByNumber) {
        Subject macro = this.createSubject(comercio, "Macroeconomía", "1010");
        Commission commission = this.createCommission(macro, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("52"),
                commission,
                Day.LUNES,
                LocalTime.of(11,0),
                LocalTime.of(14,0)
        );
    }

    private void createMicroEconomia(Degree comercio, Map<String, Classroom> classroomByNumber) {
        Subject micro = this.createSubject(comercio, "Microeconomía", "1800");
        Commission commission = this.createCommission(micro, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.SABADO,
                LocalTime.of(18,0),
                LocalTime.of(20,0)
        );
    }

    private void createPsicologia(Degree liceducacion, Map<String, Classroom> classroomByNumber) {
        Subject psicologia = this.createSubject(liceducacion, "Psicología", "1005");
        Commission commission = this.createCommission(psicologia, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.SABADO,
                LocalTime.of(14,0),
                LocalTime.of(17,0)
        );
    }

    private void createDidacticaYPedadogia(Degree liceducacion, Map<String, Classroom> classroomByNumber) {
        Subject didacticaYPedagogia = this.createSubject(liceducacion, "Didáctica y pedagogía", "1000");
        Commission commission = this.createCommission(didacticaYPedagogia, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("52"),
                commission,
                Day.SABADO,
                LocalTime.of(14,0),
                LocalTime.of(17,0)
        );
    }


    private void createHistoriaArgentina(Degree liceducacion, Map<String, Classroom> classroomByNumber) {
        Subject historiaArgetina = this.createSubject(liceducacion, "Historia Argentina", "77");
        Commission commission = this.createCommission(historiaArgetina, "Comision 1",2020,Semester.SEGUNDO);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.JUEVES,
                LocalTime.of(15,0),
                LocalTime.of(17,0)
        );
    }

    private void createBioquimica(Degree biotecnologia, Map<String, Classroom> classroomByNumber) {
        Subject analisisI = this.createSubject(biotecnologia, "Bioquímica I", "405");
        Commission commission = this.createCommission(analisisI, "Comision 1",2020,Semester.SEGUNDO);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.VIERNES,
                LocalTime.of(20,0),
                LocalTime.of(22,0)
        );
    }

    private void createMicrobioGeneral(Degree biotecnologia, Map<String, Classroom> classroomByNumber) {
        Subject microBioGeneral = this.createSubject(biotecnologia, "Microbiología general", "404");
        Commission commission = this.createCommission(microBioGeneral, "Comision 1",2020,Semester.SEGUNDO);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.VIERNES,
                LocalTime.of(20,0),
                LocalTime.of(22,0)
        );
    }

    private void createBiologiaGeneral(Degree biotecnologia, Map<String, Classroom> classroomByNumber) {
        Subject biologiaGeneral = this.createSubject(biotecnologia, "Biología general", "1251");
        Commission commission = this.createCommission(biologiaGeneral, "Comision 1",2020,Semester.SEGUNDO);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.MARTES,
                LocalTime.of(20,0),
                LocalTime.of(22,0)
        );
    }

    private void createAlgebraLineal(Degree automatizacion, Map<String, Classroom> classroomByNumber) {

        Subject algebraLineal = this.createSubject(automatizacion, "Álgebra Lineal", "999");
        Commission commission = this.createCommission(algebraLineal, "Comisión 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("52"),
                commission,
                Day.JUEVES,
                LocalTime.of(7,0),
                LocalTime.of(9,0)
        );
    }


    private Subject createAnalisisI(Degree automatizacion, Map<String, Classroom> classroomByNumber) {
        LOGGER.info("Creating Análisis matemático I Sample data");
        Subject analisisI = this.createSubject(automatizacion, "Análisis matemático I", "105");
        Commission commission = this.createCommission(analisisI, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                commission,
                Day.MARTES,
                LocalTime.of(16,0),
                LocalTime.of(18,0)
        );
        LOGGER.info("Análisis matemático I Sample Data Created");
        return analisisI;
    }

    private Subject createQuimicaOrganica(Degree biotecnologia, Map<String, Classroom> classroomByNumber) {
        LOGGER.info("Creating Química Orgánica Sample data");
        Subject quimicaOrganica = this.createSubject(biotecnologia, "Química Orgánica", "89");
        Commission quimicaOrganicaC1Miercoles = this.createCommission(quimicaOrganica, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                quimicaOrganicaC1Miercoles,
                Day.MIERCOLES,
                LocalTime.of(16,0),
                LocalTime.of(18,0)
        );
        LOGGER.info("Química Orgánica Sample Data Created");
        return quimicaOrganica;
    }

    private Subject createProgramacionObjetos3(Degree aDegree, Map<String, Classroom> classroomByNumber) {
        LOGGER.info("Creating Programacion Orientada a Objetos 3 Sample data");
        Subject progObjectos3 = this.createSubject(aDegree, "Programacion Orientada a Objetos 3", "15");
        Commission progObjetos3C1Viernes = this.createCommission(progObjectos3, "Comision 1",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                progObjetos3C1Viernes,
                Day.VIERNES,
                LocalTime.of(16,0),
                LocalTime.of(22,0)
        );
        LOGGER.info("Programacion Orientada a Objetos 3 Sample Data Created");
        return progObjectos3;
    }

    public Subject createSistemasOperativos(Degree aDegree, Map<String,Classroom> classroomByNumber){
        LOGGER.info("Creating Sistemas Operativos Sample data");
        Subject sistemasOperativos = this.createSubject(aDegree, "Sistemas Operativos", "150");
        Commission sistemasOpC1 = this.createCommission(sistemasOperativos, "Comision 1",2019,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                sistemasOpC1,
                Day.LUNES,
                LocalTime.of(16,0),
                LocalTime.of(22,0)
        );
        LOGGER.info("Sistemas Operativos Sample Data Created");
        return sistemasOperativos;
    }

    public Subject createSeguridadInformatica(Degree degree, Map<String, Classroom> classroomByNumber) {
        LOGGER.info("Creating Seguridad Informatica Sample data");
        Subject seguridadInformatica = this.createSubject(degree, "Seguridad Informática", "420");
        Commission seguridadC1 = this.createCommission(seguridadInformatica,"Comision 1",2019,Semester.PRIMER);
        this.createSchedule(classroomByNumber.get("CyT-1"),
                seguridadC1,
                Day.SABADO,
                LocalTime.of(9,0),
                LocalTime.of(13,0)
        );
        LOGGER.info("Seguridad Informatica Sample Data Created");
        return seguridadInformatica;
    }

    public Subject createTIP(Degree degree, Map<String, Classroom> clasroomByNumber) {
        LOGGER.info("Creating TIP Sample data");
        Subject tip = this.createSubject(degree, "TIP", "231");
        Commission seguridadC1 = this.createCommission(tip,"Comision 1",2019,Semester.PRIMER);
        this.createSchedule(clasroomByNumber.get("52"),
                seguridadC1,
                Day.SABADO,
                LocalTime.of(8,0),
                LocalTime.of(13,0)
        );
        LOGGER.info("TIP Sample Data Created");
        return tip;
    }

    public Subject createMatematica(Degree aDegree, Map<String,Classroom> classroomByNumber){
        LOGGER.info("Creating Matematica Sample data");
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
        LOGGER.info("Matematica Sample Data Created");
        return matematica1;
    }

    private Subject createInglesII(Degree aDegree, Map<String, Classroom> classroomByNumber) {
        LOGGER.info("Creating Ingles II Sample data");
        Subject inglesII = this.createSubject(aDegree, "Ingles II", "611");
        Commission inglesIIC1Viernes = this.createCommission(inglesII, "Comision 1",2020,Semester.PRIMER);
        this.createSchedule(
                classroomByNumber.get("CyT-1"),
                inglesIIC1Viernes,
                Day.VIERNES,
                LocalTime.of(16,0),
                LocalTime.of(22,0)
        );
        LOGGER.info("Ingles II Sample Data Created");
        return inglesII;
    }

    private Subject createSubject(Degree aDegree, String name, String code){
        LOGGER.info("Creating Subject {}(CODE{}) for degree {}",name,code,aDegree.getName());
        Subject subject = new Subject();
        subject.setName(name);
        subject.setSubjectCode(code);
        subject.addDegree(aDegree);
        aDegree.addSubject(subject);

        LOGGER.info("Subject created!");

        return subject;
    }

    private Schedule createSchedule(Classroom aClassroom, Commission aCommission, Day aday, LocalTime startTime, LocalTime endTime){
        LOGGER.info("Creating Schedule for commission {} {} in the classroom {} in the day {} at {} to {}",aCommission.getSubject().getName(),aCommission.getName(),aClassroom.getNumber(),aday,startTime,endTime);
        Schedule aSchudule = new Schedule();
        aSchudule.setDay(aday);
        aSchudule.setStartTime(startTime);
        aSchudule.setEndTime(endTime);
        aSchudule.setClassroom(aClassroom);
        aSchudule.setCommission(aCommission);
        aClassroom.addSchedule(aSchudule);
        aCommission.addSchedule(aSchudule);

        LOGGER.info("Schedule created!");

        return aSchudule;
    }

    private Commission createCommission(Subject aSubject, String name, Integer aYear, Semester aSemester){
        LOGGER.info("Creating Commission {} for Semester {}-{} for subject: {}",name,aSemester,aYear,aSubject.getName());

        Commission aCommission = new Commission();
        aCommission.setSemester(aSemester);
        aCommission.setYear(aYear);
        aCommission.setName(name);
        aCommission.setSubject(aSubject);
        aSubject.addCommission(aCommission);

        LOGGER.info("Commission created!");

        return aCommission;
    }

    private OverlapNotice createScheduleOverlap(Subject aSubject, Subject anotherSubject){
        Schedule aSchedule = aSubject.getCommissions().get(0).getSchedules().get(0);
        Schedule anotherSchedule = anotherSubject.getCommissions().get(0).getSchedules().get(0);
        return OverlapNotice.makeOverlapNotice(aSchedule,anotherSchedule);
    }
}

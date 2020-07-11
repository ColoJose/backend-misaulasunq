package com.misaulasunq.service;

import com.misaulasunq.exceptions.*;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.ClassroomRepository;
import com.misaulasunq.persistance.DegreeRepository;
import com.misaulasunq.persistance.SubjectRepository;
import com.misaulasunq.utils.fileProcessor.ExcelFileProcessor;
import com.misaulasunq.utils.fileProcessor.LoadProcessor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UploaderService {

    private final List<String> validFormats = List.of(".xlsx", ".xls");
    private final String SHEET_NAME = "Importar";
    @Autowired private DegreeRepository degreeRepository;
    @Autowired private ClassroomRepository classroomRepository;
    @Autowired private SubjectRepository subjectRepository;

    public void processSubjectHoursFile(MultipartFile fileToProcess) throws IOException, InvalidCellFormatException, InconsistentRowException, DegreeNotFoundException, ClassroomNotFoundException, NoDataHeaderException, InvalidFileExtensionException, NoSheetFoundException, DuplicateScheduleException {
        LoadProcessor loadProcessor = new LoadProcessor();
        ExcelFileProcessor xslsProcessor = new ExcelFileProcessor();

        if(!this.isValidFileExtension(fileToProcess.getOriginalFilename())){
            throw new InvalidFileExtensionException(this.validFormats);
        }

        Sheet worksheet = this.getSheet(fileToProcess);

        if(!xslsProcessor.isColumnsHeaderTheFirstRow(worksheet)) {
            throw new NoDataHeaderException();
        }

        xslsProcessor.processFile(worksheet);
        this.getRequiredObjectsFromDB(loadProcessor,xslsProcessor);
        loadProcessor.makeRelationships();
        this.subjectRepository.saveAll(loadProcessor.getSubjectsToUpsert());
    }

    private Sheet getSheet(@NotNull MultipartFile fileToProcess) throws IOException, NoSheetFoundException {
        String filename = fileToProcess.getOriginalFilename();
        Workbook workbook;

        if(filename.contains(".xlsx")) {
            workbook = new XSSFWorkbook(fileToProcess.getInputStream());
        } else {
            workbook = new HSSFWorkbook(fileToProcess.getInputStream());
        }

        return this.getSheetToImport(workbook);
    }

    private Sheet getSheetToImport(Workbook fileToProcess) throws NoSheetFoundException {
        Sheet sheetToimport = fileToProcess.getSheet(this.SHEET_NAME);

        if(sheetToimport == null) {
            throw new NoSheetFoundException(this.SHEET_NAME);
        }

        return sheetToimport;
    }

    private void getRequiredObjectsFromDB(LoadProcessor loadProcessor, ExcelFileProcessor xslsProcessor) throws DegreeNotFoundException, ClassroomNotFoundException {
        Map<String, Degree> degreesInDBByCode = this.getDegreesMappedByCode(xslsProcessor);
        Map<String, Subject> subjectsInDBByCode = this.getSubjectsMappedByCode(xslsProcessor);
        Map<String, Classroom> classroomInDBByNumber = this.getClassroomMappedByNumber(xslsProcessor);

        loadProcessor.setDegreesInDbByCode(degreesInDBByCode);
        loadProcessor.setSubjectsByCode(subjectsInDBByCode);
        loadProcessor.setClassroomInDbByNumber(classroomInDBByNumber);
        loadProcessor.setRowsToProcess(xslsProcessor.getRowsWrapperBySubjectCode());
    }

    private Map<String, Classroom> getClassroomMappedByNumber(ExcelFileProcessor xslsProcessor) {
        Map<String, Classroom> classroomsToReturn = new HashMap<>();

        List<Classroom> classrooms = this.classroomRepository
                                        .findAllByNumberInOrderByNumberAsc(
                                            xslsProcessor.getClassroomNumbers()
                                        );

        for(Classroom eachClassroom : classrooms){
            classroomsToReturn.put(eachClassroom.getNumber(),eachClassroom);
        }
        return classroomsToReturn;
    }
    private Map<String, Subject> getSubjectsMappedByCode(ExcelFileProcessor xslsProcessor) {
        Map<String, Subject> mapSubjectsToReturn = new HashMap<>();

        List<Subject> subjects = this.subjectRepository
                                    .findAllBySubjectCodeInOrderBySubjectCodeAsc(
                                        xslsProcessor.getSubjectsCodes()
                                    );

        for(Subject eachSubject : subjects){
            mapSubjectsToReturn.put(eachSubject.getSubjectCode(),eachSubject);
        }
        return mapSubjectsToReturn;
    }
    private Map<String, Degree> getDegreesMappedByCode(ExcelFileProcessor xslsProcessor){
        Map<String, Degree> mappedDegreesToReturn = new HashMap<>();

        List<Degree> degrees = this.degreeRepository
                                .findAllByCodeInOrderByCodeAsc(
                                    xslsProcessor.getDegreeCodes()
                                );

        for(Degree eachDegree : degrees){
            mappedDegreesToReturn.put(eachDegree.getCode(),eachDegree);
        }
        return mappedDegreesToReturn;
    }

    public Boolean isValidFileExtension(String originalFilename) {
        return this.validFormats
                .stream()
                .reduce(false,
                        (Boolean partialResult, String validFormat) -> {
                            return partialResult
                                    || this.isValidFormat(originalFilename,validFormat);
                        },
                        Boolean::logicalOr);
    }

    private Boolean isValidFormat(String fileNameWithExtension, String formatToCompare){
        return fileNameWithExtension.toUpperCase().contains(formatToCompare.toUpperCase());
    }
}

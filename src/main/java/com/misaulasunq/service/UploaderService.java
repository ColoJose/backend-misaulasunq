package com.misaulasunq.service;

import com.misaulasunq.exceptions.InvalidCellFormat;
import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;
import com.misaulasunq.persistance.ClassroomRepository;
import com.misaulasunq.persistance.DegreeRepository;
import com.misaulasunq.persistance.SubjectRepository;
import com.misaulasunq.utils.XSLSFileProcessor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class UploaderService {

    @Autowired
    private DegreeRepository degreeRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    private XSLSFileProcessor xslsProcessor;

    public UploaderService(){
        this.xslsProcessor = new XSLSFileProcessor();
    }

    public String processSubjectHoursFile(MultipartFile fileToProcess) throws IOException, InvalidCellFormat {
        xslsProcessor = new XSLSFileProcessor();

        Sheet worksheet = (
                new XSSFWorkbook(fileToProcess.getInputStream())
        ).getSheetAt(0);

        if(xslsProcessor.isColumnsHeaderTheFirstRow(worksheet)) {

            xslsProcessor.processFile(worksheet);
            this.makeRelationships();

        //se procesa los datos y se los mergean
            return "Procesado";
        } else {
            return "No procesado";
        }
//        Iterator<Row> rowIterator = worksheet.iterator();
//
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//            }
//        }
//        return "Procesado";
    }

    private void makeRelationships() {
        List<Degree> degreesInDB =
                degreeRepository.findAllByCodeInOrderByCodeAsc(
                        xslsProcessor.getDegreeCodes()
                );
        List<Classroom> classroomsInDB =
                classroomRepository.findAllByNumberInOrderByNumberAsc(
                        xslsProcessor.getClassroomNumbers()
                );
        List<Subject> sunjectInDB =
                subjectRepository.findAllBySubjectCodeInOrderBySubjectCodeAsc(
                        xslsProcessor.getSubjectsCodes()
                );


    }

}

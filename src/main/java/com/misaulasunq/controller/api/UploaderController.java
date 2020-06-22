package com.misaulasunq.controller.api;

import com.misaulasunq.exceptions.*;
import com.misaulasunq.service.UploaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Transactional(rollbackFor = Exception.class)
@RestController(value = "UploaderAPI")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = {RequestMethod.POST},
        maxAge = 60
)
@RequestMapping(
        name="UploaderAPI",
        value = "/uploaderAPI",
        produces = "application/json",
        method = {RequestMethod.POST}
)
@Api(tags = "Uploads Endpoint", value = "UploadEndpoint", description = "Controller para las carga masiva de materias/Horarios.")
public class UploaderController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired private UploaderService uploaderService;

    @PostMapping("/massive")
    @ApiOperation(value = "Devuelve una lista de sugerencia de las materias disponibles para buscar.")
    public ResponseEntity<String> uploadSubjectFile(@RequestBody MultipartFile file) throws InvalidCellFormatException, DegreeNotFoundException, NoDataHeaderException, IOException, InconsistentRowException, ClassroomNotFoundException, InvalidFileExtensionException, NoSheetFoundException {
        LOGGER.info("Got a POST request to Upload a file with name {}", file.getName());

        this.uploaderService.processSubjectHoursFile(file);

        ResponseEntity<String> response = new ResponseEntity<>(
                "Archivo Procesado Con Éxito.",
                HttpStatus.OK);

        LOGGER.info("Responding the request with: {}", response);
        return response;
    }
}
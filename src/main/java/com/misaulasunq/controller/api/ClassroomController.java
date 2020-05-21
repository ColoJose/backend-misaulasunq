package com.misaulasunq.controller.api;

import com.misaulasunq.service.ClassroomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController(value = "ClassroomAPI")
@Transactional
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = RequestMethod.GET,
        maxAge = 60
)
@RequestMapping(
        name="ClassroomAPI",
        value = "/classroomAPI",
        produces = "application/json",
        method = {RequestMethod.GET}
)
@Api(tags = "Classroom Endpoint", value = "ClassroomEndpoint", description = "Controller para las aulas. Sugerencias, busquedas, etc.")
public class ClassroomController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClassroomService classroomService;

    public ClassroomController() {
        LOGGER.info("Starting Classroom Controller");
    }

    @GetMapping("/suggestions")
    @ApiOperation(value = "Devuelve una lista de sugerencia de aulas disponibles para buscar.")
    public ResponseEntity<List<String>> getSuggestions(){
        LOGGER.info("Got a GET request to retrieve classroom suggestions");

        ResponseEntity<List<String>> response = new ResponseEntity<>(
                classroomService.retrieveClassroomSuggestions(),
                HttpStatus.OK
        );

        LOGGER.info("Responding the request with suggestions: {}", response);
        return response;
    }
}

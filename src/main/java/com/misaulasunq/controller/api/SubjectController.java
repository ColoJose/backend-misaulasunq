package com.misaulasunq.controller.api;

import com.misaulasunq.controller.dto.SubjectDTO;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@CrossOrigin( //Se puede configurar para que sea a travez de una clase
        origins = "http://localhost:8090",
        methods = RequestMethod.GET,
        maxAge = 60
)
@RestController(value = "SubjectAPI")
@Transactional
@RequestMapping(
        name="SubjectAPI",
        value = "/subjectAPI",
//        produces = "application/json",
        method = {RequestMethod.GET}
    )
public class SubjectController { //TODO: hay que agregar un aspecto para handlear los errores

    @GetMapping("/byClassroomNumber/{subjectId}")
    public String /*ResponseEntity<SubjectDTO>*/ getSubjectsByClassroomNumber(@PathVariable String subjectId ){

        return "{ \"body\": \"Hello\"}";
        //        try{
//            return new ResponseEntity<>(new SubjectDTO(), HttpStatus.OK);
//        }catch (Exception exception){
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
    }
}

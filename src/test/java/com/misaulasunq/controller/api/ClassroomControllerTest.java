package com.misaulasunq.controller.api;

import com.misaulasunq.persistance.ClassroomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class ClassroomControllerTest {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassroomController classroomController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(classroomController)
                .build();
    }

    @Test
    public void getAEmptyListIfDontHaveClassroomSuggestions(){
        //Setup(Given)
        classroomRepository.deleteAll();

        //Exercise(When)
        ResponseEntity<List<String>> response = classroomController.getSuggestions();

        //Test(Then)
        assertEquals(200,
                response.getStatusCodeValue(),
                "Tiene que ser una respuesta del servicio correcta");
        assertTrue("No tiene que haber sugerencias!",
                response.getBody().isEmpty());
    }

    @Test
    public void ifHaveClassroomSuggestionsTheirAreRetrieved(){
        //Setup(Given)

        //Exercise(When)
        ResponseEntity<List<String>> response = classroomController.getSuggestions();

        //Test(Then)
        assertEquals(200,
                response.getStatusCodeValue(),
                "Tiene que ser una respuesta del servicio correcta");
        assertFalse("Tiene que haber sugerencias en la respuesta!",
                response.getBody().isEmpty());
    }

    @Test
    public void ifGetClassroomSuggestionsGetAGoodResponse() throws Exception{
        //Test(then)
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/classroomAPI/suggestions")
        ).andExpect(status().isOk())
         .andExpect(content().string(containsString("52")))
         .andExpect(content().string(containsString("CyT-1")));
    }
}
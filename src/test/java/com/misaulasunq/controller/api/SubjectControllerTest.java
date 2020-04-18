package com.misaulasunq.controller.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = SubjectController.class)
@Rollback
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ifGetSubject_getAGoodResponse() throws Exception {
//        assertTrue(TestTransaction.isActive(),"No esta activada la transaccion!");
//        assertTrue(TestTransaction.isFlaggedForRollback(),"El rollback no esta activo!");

        ResultMatcher expected = MockMvcResultMatchers.content().string("{ \"body\": \"Hello\"}");

        this.mockMvc.perform(get("/subjectAPI/byClassroomNumber/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(expected);
    }
}
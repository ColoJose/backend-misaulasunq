package com.misaulasunq.controller.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UploaderController.class)
public class UploaderControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UploaderController uploaderController;

    @Test
    public void uploadSubjectFile() throws Exception {
        //Setuo(Given)
        MockMultipartFile  file =
            new MockMultipartFile(
                "Import Sample Test",
                "Import Sample Test.xlsx",
                String.valueOf(MediaType.MULTIPART_FORM_DATA),
                this.getClass().getClassLoader().getResourceAsStream("Import Sample Test.xlsx")
            );

        //Test(then)
        this.mockMvc.perform(
            MockMvcRequestBuilders.multipart("/uploaderAPI/massive").file(file)
        ).andExpect(status().isOk());
    }
}
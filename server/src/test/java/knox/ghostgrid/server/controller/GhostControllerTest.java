package knox.ghostgrid.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GhostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSpawnSuccessAndFailure() throws Exception {
        String user = "uniqueGhost" + System.currentTimeMillis();

        // First spawn should succeed
        mockMvc.perform(post("/ghosts/spawn")
                .param("user", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.user").value(user));

        // Second spawn with same user should fail
        mockMvc.perform(post("/ghosts/spawn")
                .param("user", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("User is already active"));
    }
}

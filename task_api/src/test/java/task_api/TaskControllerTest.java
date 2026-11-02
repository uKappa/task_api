package task_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldCreateTaskFromJsonRequest() throws Exception {
        String requestJson = """
            {
                "title": "Learn Spring Boot",
                "description": "Build my first REST API"
            }
            """;
        Task createdTask = new Task(1L, "Learn Spring Boot", "Build my first REST API");

        when(taskService.addTask("Learn Spring Boot", "Build my first REST API"))
            .thenReturn(createdTask);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
            .andExpect(jsonPath("$.description").value("Build my first REST API"))
            .andExpect(jsonPath("$.status").value("TODO"));
    }
}

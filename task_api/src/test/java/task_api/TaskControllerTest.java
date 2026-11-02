package task_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/tasks/1")))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
            .andExpect(jsonPath("$.description").value("Build my first REST API"))
            .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void shouldRejectTaskWithBlankTitle() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "   ",
                        "description": "Build my first REST API"
                    }
                    """))
            .andExpect(status().isBadRequest());

        verify(taskService, never()).addTask("   ", "Build my first REST API");
    }

    @Test
    void shouldReturnTaskWhenTaskExists() throws Exception {
        Task task = new Task(1L, "Learn Spring Boot", "Build my first REST API");
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
            .andExpect(jsonPath("$.description").value("Build my first REST API"))
            .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/{id}", 999L))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTaskStatusWhenTaskExists() throws Exception {
        when(taskService.updateTask(1L, TaskStatus.IN_PROGRESS)).thenReturn(true);

        mockMvc.perform(patch("/tasks/{id}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"status": "IN_PROGRESS"}
                    """))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingStatusOfMissingTask() throws Exception {
        when(taskService.updateTask(999L, TaskStatus.DONE)).thenReturn(false);

        mockMvc.perform(patch("/tasks/{id}/status", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"status": "DONE"}
                    """))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsMissing() throws Exception {
        mockMvc.perform(patch("/tasks/{id}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test 
    void shouldDeleteTaskWhenTaskExists() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/tasks/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingTask() throws Exception {
        when(taskService.deleteTask(999L)).thenReturn(false);

        mockMvc.perform(delete("/tasks/{id}", 999L))
            .andExpect(status().isNotFound());
    }

}

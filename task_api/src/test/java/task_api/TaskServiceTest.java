
package task_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService();
    }

    @Test
    void shouldCreateTask() {
        Task task = service.addTask("Test");

        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Test", task.getTitle());
        assertEquals(TaskStatus.TODO, task.getStatus());
    }

    @Test
    void shouldFindTaskById() {
        Task created = service.addTask("Test");

        var result = service.getTaskById(created.getId());

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenTaskDoesNotExist() {
        var result = service.getTaskById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task task = service.addTask("Test");

        boolean updated = service.updateTask(
            task.getId(),
            TaskStatus.IN_PROGRESS
        );

        assertTrue(updated);

        var result = service.getTaskById(task.getId());

        assertTrue(result.isPresent());
        assertEquals(
            TaskStatus.IN_PROGRESS,
            result.get().getStatus()
        );
    }

    @Test
    void shouldReturnFalseWhenUpdatingMissingTask() {
        boolean updated = service.updateTask(
            999L,
            TaskStatus.DONE
        );

        assertFalse(updated);
    }

    @Test
    void shouldDeleteTask() {
        Task task = service.addTask("Test");

        boolean deleted = service.deleteTask(task.getId());

        assertTrue(deleted);
        assertTrue(service.getTaskById(task.getId()).isEmpty());
    }
}

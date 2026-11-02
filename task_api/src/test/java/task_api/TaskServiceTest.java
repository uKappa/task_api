
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
    void shouldReturnEmptyCollectionWhenNoTasksExist() {
        assertTrue(service.getAllTasks().isEmpty());
    }

    @Test
    void shouldReturnAllTasks() {
        Task first = service.addTask("Learn Java",null);
        Task second = service.addTask("Learn JUnit",null);

        var tasks = service.getAllTasks();

        assertEquals(2, tasks.size());
        assertTrue(tasks.containsKey(first.getId()));
        assertTrue(tasks.containsKey(second.getId()));
    }

    @Test
    void shouldReturnCopyOfTaskCollection() {
        Task task = service.addTask("Learn Java",null);

        var tasks = service.getAllTasks();
        tasks.clear();

        assertTrue(service.getTaskById(task.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingTask() {
        assertFalse(service.deleteTask(999L));
    }

    @Test
    void shouldRejectNullTitle() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addTask(null, null)
        );
    }

    @Test
    void shouldRejectEmptyTitle() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addTask("", null)
        );
    }

    @Test
    void shouldRejectBlankTitle() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.addTask("   ", null)
        );
    }

    @Test
    void shouldReturnFalseWhenUpdatingMissingTask() {
        assertFalse(
            service.updateTask(999L, TaskStatus.DONE)
        );
    }
}

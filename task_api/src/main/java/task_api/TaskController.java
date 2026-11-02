package task_api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public Map<Long, Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody CreateTaskRequest request) {
        return taskService.addTask(request.getTitle(), request.getDescription());
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/tasks/{id}/status")
    public ResponseEntity<Void> updateTaskStatus(
            @PathVariable("id") Long id,
            @RequestBody UpdateTaskStatusRequest request) {
        if (request.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (taskService.updateTask(id, request.getStatus())) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
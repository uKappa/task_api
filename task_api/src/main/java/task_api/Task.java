package task_api;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;

    Task(Long id, String title, String description){
        
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        status = TaskStatus.TODO;
        createdAt = LocalDateTime.now();

    }

    void updateStatus(TaskStatus status){

        this.status = status;

    }

}

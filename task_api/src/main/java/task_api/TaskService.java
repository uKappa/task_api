package task_api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private Long id;
    private Map<Long, Task> taskCollection;
    
    TaskService(){

        taskCollection = new HashMap<>();
        id = 1L;
        
    }

    Task addTask(String title, String description){

        Task newTask = new Task(id, title, description);
        
        taskCollection.put(id, newTask);
        id += 1;

        return newTask;

    }

    Map<Long, Task> getAllTasks(){ 
        
        Map<Long, Task> collectionCopy = new HashMap<>();
        collectionCopy.putAll(taskCollection);
        return collectionCopy; 
    
    }

    Optional<Task> getTaskById(Long id){ return Optional.ofNullable(taskCollection.get(id)); }

    boolean updateTask(Long id, TaskStatus status) {

        Task task = taskCollection.get(id);

        if (task == null) return false;
        
        task.updateStatus(status);
        return true;

    }

    boolean deleteTask(Long id){

        Task task = taskCollection.remove(id);

        if (task == null) return false;
        
        return true;

    }

}

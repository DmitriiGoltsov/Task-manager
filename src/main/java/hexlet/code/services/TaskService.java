package hexlet.code.services;

import hexlet.code.dto.TaskDTO;
import hexlet.code.models.Task;
import com.querydsl.core.types.Predicate;


public interface TaskService {
    
    Task createNewTask(TaskDTO taskDto);
    
    Iterable<Task> getAllTasksByCriteria(Predicate predicate);
    
    Task getTaskById(Long id);
    
    Task updateTask(Long id, TaskDTO taskDto);
    
    void deleteTaskById(Long id);
    
}

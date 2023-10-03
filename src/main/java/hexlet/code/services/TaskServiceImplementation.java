package hexlet.code.services;

import hexlet.code.dto.TaskDTO;
import hexlet.code.models.Task;
import hexlet.code.models.TaskStatus;
import hexlet.code.models.User;
import hexlet.code.repositories.TaskRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImplementation implements TaskService {
    
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskStatusServiceImpl taskStatusService;
    
    @Override
    public Task createNewTask(TaskDTO taskDto) {
        Task task = new Task();
        formTaskFromDto(taskDto, task);
        return taskRepository.save(task);
    }
    
    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
    }
    
    @Override
    public Task updateTask(Long id, TaskDTO taskDto) {
        Task taskToUpdate = getTaskById(id);
        formTaskFromDto(taskDto, taskToUpdate);
        return taskRepository.save(taskToUpdate);
    }
    
    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    private void formTaskFromDto(TaskDTO taskDTO, Task task) {
        final User author = userService.getUserById(taskDTO.getAuthorId());
        final User executor = userService.getUserById(taskDTO.getExecutorId());
        final TaskStatus taskStatus = taskStatusService.getTaskStatusById(taskDTO.getTaskStatusId());
        
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
    }
}

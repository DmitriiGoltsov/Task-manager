package hexlet.code.services;

import com.querydsl.core.types.Predicate;

import hexlet.code.dto.TaskDTO;
import hexlet.code.models.Label;
import hexlet.code.models.Task;
import hexlet.code.models.TaskStatus;
import hexlet.code.models.User;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.repositories.TaskRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskStatusService taskStatusService;
    private final LabelRepository labelRepository;

    @Override
    public Task createNewTask(TaskDTO taskDto) {
        Task task = new Task();
        formTaskFromDto(taskDto, task);
        return taskRepository.save(task);
    }

    @Override
    public Iterable<Task> getAllTasksByCriteria(Predicate predicate) {
        return taskRepository.findAll(predicate);
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

        final User author = userService.getCurrentUser();
        final TaskStatus taskStatus = taskStatusService.getTaskStatusById(taskDTO.getTaskStatusId());
        final String taskName = taskDTO.getName();
        final Set<Label> labels = Optional.ofNullable(taskDTO.getLabelIds())
                .map(labelIds -> new HashSet<>(labelRepository.findAllById(labelIds)))
                .orElseGet(HashSet::new);

        task.setAuthor(author);
        task.setName(taskName);
        task.setTaskStatus(taskStatus);
        task.setLabels(labels);

        Optional.ofNullable(taskDTO.getDescription())
                .ifPresentOrElse(task::setDescription, () -> task.setDescription(""));

        if (Optional.ofNullable(taskDTO.getExecutorId()).isPresent()) {
            task.setExecutor(userService.getUserById(taskDTO.getExecutorId()));
        }
    }
}

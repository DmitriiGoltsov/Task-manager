package hexlet.code.services;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.models.TaskStatus;
import hexlet.code.repositories.TaskStatusRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Transactional
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository repository;

    public List<TaskStatus> getAllTaskStatuses() {
        return repository.findAll();
    }

    public TaskStatus createTaskStatus(TaskStatusDTO taskStatusDTO) {
        final TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDTO.getName());

        return repository.save(taskStatus);
    }

    public TaskStatus getTaskStatusById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task status with id " + id + " not found"));
    }

    public TaskStatus updateTaskStatus(final Long id, final TaskStatusDTO taskStatusDTO) {
        final TaskStatus taskStatusToUpdate = getTaskStatusById(id);
        taskStatusToUpdate.setName(taskStatusDTO.getName());

        return repository.save(taskStatusToUpdate);
    }

    public void deleteTaskStatus(Long id) {
        repository.deleteById(id);
    }

    public void deleteAllTaskStatuses() {
        repository.deleteAll();
    }

}

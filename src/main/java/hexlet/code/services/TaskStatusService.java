package hexlet.code.services;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.models.TaskStatus;

import java.util.List;

public interface TaskStatusService {
    TaskStatus getTaskStatusById(Long id);
    List<TaskStatus> getAllStatuses();
    TaskStatus createTaskStatus(TaskStatusDTO taskStatusDTO);
    TaskStatus updateTaskStatus(Long id, TaskStatusDTO taskStatusDTO);
    void deleteTaskStatus(Long id);
}

package hexlet.code.services;

import hexlet.code.models.TaskStatus;

public interface TaskStatusService {
    TaskStatus getTaskStatusById(Long id);
}

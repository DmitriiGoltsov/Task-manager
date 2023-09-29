package hexlet.code.services;

import hexlet.code.models.TaskStatus;

import java.util.Optional;

public interface TaskStatusService {
    TaskStatus getTaskStatusById(Long id);
}

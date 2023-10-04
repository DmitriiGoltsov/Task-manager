package hexlet.code.controllers;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDTO;
import hexlet.code.models.Task;
import hexlet.code.services.TaskServiceImplementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

import static hexlet.code.controllers.TaskController.TASK_CONTROLLER_URL;


@Tag(name = "Task controller")
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_URL)
@RequiredArgsConstructor
public class TaskController {
    
    protected static final String TASK_CONTROLLER_URL = "/tasks";
    
    private final TaskServiceImplementation taskService;
    
    private static final String ONLY_OWNER_BY_ID =
            "@taskService.getTaskById(#id).getAuthor().getEmail() == authentication.getName()";
    
    
    @Operation(description = "Get all tasks of all authors")
    @ApiResponse(responseCode = "200", description = "Every task is loaded")
    @GetMapping
    public Iterable<Task> getAllTasksByCriteria(@QuerydslPredicate(root = Task.class) final Predicate predicate) {
        return taskService.getAllTasksByCriteria(predicate);
    }
    
    @Operation(description = "Get a particular task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task was found and loaded"),
            @ApiResponse(responseCode = "404", description = "Task with such id does not exist")
    })
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable("id") @Valid final Long id) {
        return taskService.getTaskById(id);
    }
    
    
    @Operation(description = "Create a new task")
    @ApiResponse(responseCode = "201", description = "Task was successfully created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Task createTask(@RequestBody final TaskDTO taskDTO) {
        return taskService.createNewTask(taskDTO);
    }
    
    @Operation(description = "Update task by uts id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "404", description = "Task with this ID not found")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable("id") @Valid final Long id,
                           @RequestBody @Valid final TaskDTO taskDTO) {
        
        return taskService.updateTask(id, taskDTO);
    }
    
    @Operation(description = "Delete task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was deleted"),
            @ApiResponse(responseCode = "404", description = "Task with such id is not found")
    })
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") @Valid final Long id) {
        taskService.deleteTaskById(id);
    }
}

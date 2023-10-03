package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    private String description;
    
    @NotNull(message = "Task status cannot be blank or null")
    private Long taskStatusId;
    
    @NotNull(message = "Every task has to have an author")
    private Long authorId;
    
    @NotNull(message = "Every task has to have an executor")
    private Long executorId;
    
}

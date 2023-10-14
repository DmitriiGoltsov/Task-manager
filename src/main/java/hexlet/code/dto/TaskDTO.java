package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Task status cannot be blank or null")
    private Long taskStatusId;

    private Long authorId;

    private Long executorId;

    private Set<Long> labelIds;
}

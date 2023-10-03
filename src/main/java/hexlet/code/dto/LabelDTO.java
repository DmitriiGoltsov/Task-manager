package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelDTO {

    @NotBlank(message = "Label's name cannot be blank")
    private String name;
    
}

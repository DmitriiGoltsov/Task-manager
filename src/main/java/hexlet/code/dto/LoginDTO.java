package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email/username cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 255, message = "Password has to contain from 3 to 255 characters!")
    private String password;

}

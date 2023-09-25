package hexlet.code.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequestDTO {

    private String email;
    private String password;

}

package hexlet.code.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class UserDTO {

    @Setter(AccessLevel.NONE)
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}

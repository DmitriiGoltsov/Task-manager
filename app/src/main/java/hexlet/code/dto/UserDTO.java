package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}

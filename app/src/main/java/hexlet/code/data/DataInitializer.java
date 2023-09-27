package hexlet.code.data;

import hexlet.code.dto.UserDTO;
import hexlet.code.services.CustomUserDetailsService;
import hexlet.code.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    public static final String DEFAULT_EMAIL = "admin@gmail.com";
    public static final String DEFAULT_PASSWORD = "qwerty";


    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setPassword(DEFAULT_PASSWORD);

        userService.createUser(userDTO);
    }
}
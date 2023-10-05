package hexlet.code.data;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;
import hexlet.code.services.CustomUserDetailsService;
import hexlet.code.services.TaskServiceImplementation;
import hexlet.code.services.TaskStatusServiceImpl;
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
    private static final String DEFAULT_NAME = "Admin";
    private static final String DEFAULT_SURNAME = "Adminovich";

    private static final String DEFAULT_TASK_STATUS = "INITIALIZED";


    private final UserService userService;
    private final TaskStatusServiceImpl taskStatusService;
    private final TaskServiceImplementation taskService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setPassword(DEFAULT_PASSWORD);
        userDTO.setFirstName(DEFAULT_NAME);
        userDTO.setLastName(DEFAULT_SURNAME);

        User user = userService.createUser(userDTO);
    }
}

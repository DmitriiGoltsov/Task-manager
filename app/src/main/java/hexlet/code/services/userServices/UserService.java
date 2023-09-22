package hexlet.code.services.userServices;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;
import jakarta.xml.bind.ValidationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    User createUser(UserDTO userDTO) throws ValidationException;
    User getUserById(Long id);
    User getUserByEmail(String email);
    User updateUserById(Long id, UserDTO userDTO);
    void deleteUserById(Long id);
    List<User> getAllUsers();
    User getUserDetailsBy(String email);

}

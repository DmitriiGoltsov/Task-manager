package hexlet.code.services.userServices;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;

import java.util.List;

public interface UserService {

    User createUser(UserDTO userDTO);
    User getUserById(Long id);
    User updateUserById(Long id, UserDTO userDTO);
    void deleteUserById(Long id);
    List<User> getAllUsers();

}

package hexlet.code.services.userServices;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;
import hexlet.code.repositories.UserRepository;
import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return  userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + "was not found!"));
    }

    @Override
    public User updateUserById(Long id, UserDTO userDTO) {
        User oldUser = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + " was not found!"));

        oldUser.setFirstName(userDTO.getFirstName());
        oldUser.setLastName(userDTO.getLastName());
        oldUser.setEmail(userDTO.getEmail());
        oldUser.setPassword(userDTO.getPassword());

        return userRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + " was not found!"));

        userRepository.delete(userToDelete);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

//    @Override
//    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//        return userRepository.findUserByEmail(email);
//    }
//
//    private UserDetails buildSpringUser(final User user) {
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
//    }

}

package hexlet.code.services.userServices;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;
import hexlet.code.repositories.UserRepository;
import jakarta.xml.bind.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDTO userDTO) throws ValidationException {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new ValidationException("User with username " + userDTO.getEmail() + " already exists");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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

        if (userDTO.getFirstName() != null) {
            oldUser.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            oldUser.setLastName(userDTO.getLastName());
        }

        if (userDTO.getEmail() != null) {
            oldUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + " was not found!"));

        userRepository.delete(userToDelete);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " was not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User getUserDetailsBy(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found"));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username/email " + username + " was not found"));

        String userRole = user.getUserRole().name();

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRole));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}

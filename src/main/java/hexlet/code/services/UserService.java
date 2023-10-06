package hexlet.code.services;

import hexlet.code.repositories.UserRepository;

import lombok.AllArgsConstructor;

import jakarta.validation.ValidationException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hexlet.code.models.User;
import hexlet.code.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public User getUserById(Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + "was not found!"));
    }

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

    public void deleteUserById(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + " was not found!"));

        userRepository.delete(userToDelete);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found"));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUserDetailsBy(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found"));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public String getEmailOfCurrentUser() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    public User getCurrentUser() {
        String userEmail = getEmailOfCurrentUser();
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + userEmail + " not found"));
    }


}

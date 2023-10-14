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
import java.util.Optional;

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
        User userToBeUpdated = getUserById(id);

        Optional<String> newFirstName = Optional.ofNullable(userDTO.getFirstName());
        Optional<String> newLastName = Optional.ofNullable(userDTO.getLastName());
        Optional<String> newEmail = Optional.ofNullable(userDTO.getEmail());
        Optional<String> newPassword = Optional.ofNullable(passwordEncoder.encode(userDTO.getPassword()));

        newFirstName.ifPresent(userToBeUpdated::setFirstName);
        newLastName.ifPresent(userToBeUpdated::setLastName);
        newEmail.ifPresent(userToBeUpdated::setEmail);
        newPassword.ifPresent(userToBeUpdated::setPassword);

        return userRepository.save(userToBeUpdated);
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

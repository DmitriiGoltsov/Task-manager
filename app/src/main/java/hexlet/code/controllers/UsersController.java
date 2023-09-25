package hexlet.code.controllers;

import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;

import hexlet.code.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.xml.bind.ValidationException;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Tag(name = "User controller")
@RestController
@AllArgsConstructor
@RequestMapping(path = "${base-url}")
public class UsersController {

    @Autowired
    private final UserService userService;

    @Operation(summary = "Operation allows to get all users stored in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users were successfully found and returned"),
            @ApiResponse(responseCode = "404", description = "There are not stored users in the DB")
    })
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> result = userService.getAllUsers();
        if (result.isEmpty()) {
            throw new RuntimeException("There are not stored users in the DB");
        }
        return userService.getAllUsers();
    }

    @Operation(summary = "Operation adds a new user to the DB")
    @ApiResponse(responseCode = "201", description = "User was successfully created")
    @PostMapping("/users")
    public User createUser(@RequestBody UserDTO userDTO) throws ValidationException {
        return userService.createUser(userDTO);
    }

    @Operation(summary = "Operation gets a particular user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found"),
            @ApiResponse(responseCode = "404", description = "User with such id has not been found")
    })
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Operation changes characteristics of the particular user that is found by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found and updated"),
            @ApiResponse(responseCode = "404", description = "User with such id has not been found")
    })
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody UserDTO userDTO) {

        return userService.updateUserById(id, userDTO);
    }

    @Operation(summary = "Operation deletes the particular user with given id from the DB")
    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}

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

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

import static hexlet.code.controllers.UsersController.USER_CONTROLLER_PATH;

@Tag(name = "User controller")
@RestController
@AllArgsConstructor
@RequestMapping(path = "${base-url}" + USER_CONTROLLER_PATH)
public class UsersController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";

    private final UserService userService;

    @Operation(summary = "Operation to get all users stored in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users were successfully found and loaded"),
            @ApiResponse(responseCode = "404", description = "There are not stored users in the DB")
    })
    @GetMapping
    public List<User> getAllUsers() {
        List<User> result = userService.getAllUsers();
        if (result.isEmpty()) {
            throw new RuntimeException("There are not stored users in the DB");
        }
        return result;
    }

    @Operation(summary = "Operation adds a new user to the DB")
    @ApiResponse(responseCode = "201", description = "User was successfully created")
    @PostMapping
    public User createUser(@RequestBody UserDTO userDTO) throws ValidationException {
        return userService.createUser(userDTO);
    }

    @Operation(summary = "Operation gets a particular user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found"),
            @ApiResponse(responseCode = "404", description = "User with such id has not been found")
    })
    @GetMapping(ID)
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Operation changes characteristics of the particular user that is found by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found and updated"),
            @ApiResponse(responseCode = "404", description = "User with such id has not been found")
    })
    @PutMapping(ID)
    public User updateUser(@PathVariable Long id,
                           @RequestBody UserDTO userDTO) {

        return userService.updateUserById(id, userDTO);
    }

    @Operation(summary = "Operation deletes the particular user with given id from the DB")
    @DeleteMapping(ID)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}

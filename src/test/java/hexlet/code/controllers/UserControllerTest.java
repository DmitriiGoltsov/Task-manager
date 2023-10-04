package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.LoginDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.models.User;
import hexlet.code.repositories.UserRepository;
import hexlet.code.services.UserService;
import hexlet.code.utils.TestUtils;
import hexlet.code.utils.NamePaths;

import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;
import static hexlet.code.controllers.UsersController.ID;
import static hexlet.code.controllers.UsersController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.NEW_USERNAME;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    private static final String BASE_URL = "/api";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    public void beforeEach() throws Exception {
        testUtils.tearDown();
        testUtils.registerDefaultUser();
    }

    @AfterEach
    public void afterEach() {
        testUtils.tearDown();
    }

    @Test
    public void registrationTest() throws Exception {
        assertEquals(0, userService.getAllUsers().size());
        testUtils.registerDefaultUser().andExpect(status().isCreated());
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        final User expectedUser = userService.getAllUsers().get(0);
        final var response = testUtils.perform(
                        get(BASE_URL + USER_CONTROLLER_PATH + ID, expectedUser.getId()),
                        expectedUser.getEmail())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        final User user = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
    }

    @Test
    public void getAllUsers() throws Exception {
        final MockHttpServletResponse response = testUtils.perform(
                get(BASE_URL + USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() { });
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("email@email.com");
    }

    @Test
    public void loginTest() throws Exception {
        final LoginDTO loginDTO = new LoginDTO(
                testUtils.getTestRegistrationDto().getEmail(),
                testUtils.getTestRegistrationDto().getPassword()
        );

        final MockHttpServletRequestBuilder loginRequest = post(BASE_URL + NamePaths.getLoginPath())
                .content(asJson(loginDTO))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(loginRequest).andExpect(status().isOk());
    }

    @Test
    public void updateUserTest() throws Exception {
        final long userId = userService.getUserByEmail(TEST_USERNAME).getId();

        final UserDTO userDTO = new UserDTO(
                "new name",
                "new surname",
                NEW_USERNAME,
                "password"
        );

        final MockHttpServletRequestBuilder updateRequest =
                put(BASE_URL + USER_CONTROLLER_PATH + ID, userId)
                .content(asJson(userDTO))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findUserByEmail(TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findUserByEmail(NEW_USERNAME).orElse(null));
    }

    @Test
    public void deleteUser() throws Exception {
        final Long userId = userService.getUserByEmail(TEST_USERNAME).getId();

        testUtils.perform(delete(USER_CONTROLLER_PATH + ID, userId), TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(1, userRepository.count());
    }
}

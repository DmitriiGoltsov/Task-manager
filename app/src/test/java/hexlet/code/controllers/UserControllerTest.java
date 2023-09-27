package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.dto.UserDTO;
import hexlet.code.services.UserService;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/data.sql"})
public class UserControllerTest {

    private static String EXISTING_EMAIL = "Saladdin@gmail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    void testWelcomePage() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/welcome"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentAsString()).contains("Welcome to Spring");
    }

    @Test
    void getAllUsersTest() throws Exception {

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/users"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo("application/json");

        assertThat(response.getContentAsString()).contains("John", "Smith", "john@gmail.com");
        assertThat(response.getContentAsString()).doesNotContain("12345");

        assertThat(response.getContentAsString()).contains("Jack", "Doe", "jack@mail.com");
        assertThat(response.getContentAsString()).doesNotContain("qwerty");

        assertThat(response.getContentAsString()).contains("Jassica", "Simpson", "jassika@yahoo.com");
        assertThat(response.getContentAsString()).doesNotContain("0000");

        assertThat(response.getContentAsString()).contains("Richard", "Lionheart", EXISTING_EMAIL);
        assertThat(response.getContentAsString()).doesNotContain("richard");
    }

    @Test
    void getUserTest() throws Exception {

        long id = userService.getUserByEmail(EXISTING_EMAIL).getId();

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/users/" + id))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo("application/json");

        assertThat(response.getContentAsString()).contains("Richard", "Lionheart", EXISTING_EMAIL);
        assertThat(response.getContentAsString()).doesNotContain("richard");
    }

    @Test
    void updateUserTest() throws Exception {

        long id = userService.getUserByEmail(EXISTING_EMAIL).getId();

        UserDTO userDTO = new UserDTO();
        String newLastName = "Plantagenet";
        userDTO.setLastName(newLastName);

        mockMvc.perform(put("/api/users/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userDTO)));

        MockHttpServletResponse getResponse = mockMvc
                .perform(get("/api/users/{id}", id))
                .andReturn()
                .getResponse();

        assertThat(getResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(getResponse.getContentType()).isEqualTo("application/json");

        assertThat(getResponse.getContentAsString())
                .contains("Richard", "Plantagenet", "saladinisafriend@templemail.uk");
        assertThat(getResponse.getContentAsString()).doesNotContain("Lionheart", "Acra");
    }

    @Test
    void createUserTest() throws Exception {
        String firstName = "Igor";
        String lastName = "Ott";
        String email = "ott@gmail.com";
        String password = "password";

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);

        MockHttpServletResponse response = mockMvc
                .perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userDTO)))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo("application/json");

        assertThat(response.getContentAsString()).contains(firstName, lastName, email);
        assertThat(response.getContentAsString()).doesNotContain(password);
    }

    @Test
    void deleteUserTest() throws Exception {

        long existingId = userService.getUserByEmail(EXISTING_EMAIL).getId();

        MockHttpServletResponse response = mockMvc.perform(delete("/api/users/{id}", existingId))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentAsString())
                .doesNotContain(EXISTING_EMAIL, "Richard", "Lionheart", "Acra");
    }
}

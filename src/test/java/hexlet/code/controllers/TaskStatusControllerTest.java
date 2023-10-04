package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.models.TaskStatus;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.services.TaskStatusServiceImpl;
import hexlet.code.utils.TestUtils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;
import static hexlet.code.controllers.TaskStatusController.TASK_STATUS_URL;

import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.asJson;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
public class TaskStatusControllerTest {

    private static final String BASE_URL = "/api";

    @Autowired
    private TaskStatusServiceImpl taskStatusService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

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
    public void getAllStatusesTest() throws Exception {

        final MockHttpServletResponse response = testUtils.perform(
                        get(BASE_URL + TASK_STATUS_URL),
                        TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> statuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<TaskStatus> expectedStatuses = taskStatusRepository.findAll();

        Assertions.assertThatList(statuses).isEqualTo(expectedStatuses);
    }

    @Test
    public void createNewTaskStatusTest() throws Exception {
        final TaskStatusDTO taskStatusDTO = new TaskStatusDTO("New task status");
        final MockHttpServletRequestBuilder request = post(BASE_URL + TASK_STATUS_URL)
                .content(asJson(taskStatusDTO))
                .contentType(MediaType.APPLICATION_JSON);
        final MockHttpServletResponse response = testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final TaskStatus savedTaskStatus = fromJson(response.getContentAsString(), new TypeReference<>() { });
        assertThat(taskStatusRepository.getReferenceById(savedTaskStatus.getId())).isNotNull();
    }
}

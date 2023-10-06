package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.models.Label;
import hexlet.code.models.Task;
import hexlet.code.models.TaskStatus;
import hexlet.code.models.User;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.repositories.TaskRepository;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.services.LabelServiceImplementation;
import hexlet.code.services.TaskServiceImplementation;
import hexlet.code.services.TaskStatusServiceImpl;
import hexlet.code.services.UserService;
import hexlet.code.utils.TestUtils;

import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.stream.IntStream;

import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;
import static hexlet.code.controllers.TaskController.TASK_CONTROLLER_URL;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringTestConfig.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(TEST_PROFILE)
public class TaskControllerTest {

    private static final String BASE_URL = "/api";
    private TaskStatusDTO taskStatusDTO;
    private TaskStatus taskStatus;

    private TaskDTO taskDTO;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskServiceImplementation taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelServiceImplementation labelService;

    @Autowired
    private TaskStatusServiceImpl taskStatusService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void beforeEach() throws Exception {
        testUtils.tearDown();
        testUtils.registerDefaultUser();
        taskStatusDTO = new TaskStatusDTO("Test status");
        taskStatus = taskStatusService.createTaskStatus(taskStatusDTO);
        taskDTO = createInitialTaskDTOForTests();
    }

    @AfterEach
    public void afterEach() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }

    @Test
    public void getAllTasksTest() throws Exception {

        final List<Task> expectedTasks = IntStream.range(0, 10)
                .mapToObj(i -> Task.builder()
                        .author(testUtils.getUserByEmail(TEST_USERNAME))
                        .description("description" + i)
                        .name("someName" + i)
                        .taskStatus(taskStatus)
                        .build())
                .toList();

        taskRepository.saveAll(expectedTasks);

        final MockHttpServletResponse response = testUtils.perform(get(
                BASE_URL + TASK_CONTROLLER_URL), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> actualTasks = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(expectedTasks).hasSize(actualTasks.size());
    }

    @Test
    public void getTaskById() throws Exception {

        final Task expected = taskRepository.save(Task.builder()
                .author(testUtils.getUserByEmail(TEST_USERNAME))
                .description("description")
                .name("name")
                .taskStatus(taskStatus)
                .build()
        );

        final Long id = expected.getId();

        final MockHttpServletRequestBuilder request = get(BASE_URL + TASK_CONTROLLER_URL + "/" + id, id);

        final MockHttpServletResponse response = testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task actual = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
    }

    @Test
    public void createTaskTest() throws Exception {
        createTaskForTest(taskDTO).andExpect(status().isCreated());
        assertFalse(taskRepository.findAll().isEmpty());
    }

    @Test
    public void updateTaskTest() throws Exception {

        final MockHttpServletResponse response = createTaskForTest(taskDTO)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final Task createdTask = fromJson(response.getContentAsString(), new TypeReference<>() { });

        long id = createdTask.getId();

        final TaskDTO updatedTaskDTO = new TaskDTO(
                "Updated task",
                "Updated description",
                createdTask.getTaskStatus().getId(),
                createdTask.getAuthor().getId(),
                createdTask.getExecutor().getId(),
                List.of()
        );

        final MockHttpServletRequestBuilder updatingRequest = put(
                BASE_URL + TASK_CONTROLLER_URL + "/" + id, id)
                .content(asJson(updatedTaskDTO))
                .contentType(APPLICATION_JSON);

        testUtils.perform(updatingRequest, TEST_USERNAME).andExpect(status().isOk());
    }

    @Test
    public void deleteTaskTest() throws Exception {

        final Task task = taskRepository.save(Task.builder()
                .name("t name")
                .description("desc")
                .author(testUtils.getUserByEmail(TEST_USERNAME))
                .taskStatus(taskStatus)
                .build());

        final long id = task.getId();

        testUtils.perform(delete(BASE_URL + TASK_CONTROLLER_URL + "/" + id, id), TEST_USERNAME)
                .andExpect(status().isOk());

        assertFalse(taskRepository.existsById(task.getId()));
    }


    private ResultActions createTaskForTest(final TaskDTO taskDTO) throws Exception {
        final MockHttpServletRequestBuilder request = post(BASE_URL + TASK_CONTROLLER_URL)
                .content(asJson(taskDTO))
                .contentType(APPLICATION_JSON);

        return testUtils.perform(request, TEST_USERNAME);
    }

    private TaskDTO createInitialTaskDTOForTests() throws ValidationException {

        final User user = userService.getUserByEmail(TEST_USERNAME);

        final TaskStatus taskStatus = taskStatusService.createTaskStatus(taskStatusDTO);

        final Label label1 = labelService.createNewLabel(new LabelDTO("First label"));
        final Label label2 = labelService.createNewLabel(new LabelDTO("Second label"));

        return new TaskDTO(
                "test task",
                "test description",
                taskStatus.getId(),
                user.getId(),
                user.getId(),
                List.of(label1.getId(), label2.getId())
        );
    }
}

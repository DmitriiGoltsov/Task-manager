package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import hexlet.code.config.SpringTestConfig;
import hexlet.code.dto.LabelDTO;
import hexlet.code.models.Label;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.services.LabelServiceImplementation;
import hexlet.code.utils.TestUtils;

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
import java.util.stream.IntStream;

import static hexlet.code.controllers.LabelController.LABEL_CONTROLLER_URL;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;

import static hexlet.code.config.SpringTestConfig.TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
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
public class LabelControllerTest {

    private static final String BASE_URL = "/api";

    private LabelDTO labelDTO;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelServiceImplementation labelService;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    public void beforeEach() throws Exception {
        testUtils.tearDown();
        testUtils.registerDefaultUser();
        labelDTO = new LabelDTO("Test label");
    }

    @AfterEach
    public void afterEach() {
        labelRepository.deleteAll();
    }

    @Test
    public void getAllLabelsTest() throws Exception {

        final List<Label> expectedLabels = IntStream.range(0, 10)
                .mapToObj(i -> Label.builder()
                        .name("label" + " " + i)
                        .build())
                .toList();

        labelRepository.saveAll(expectedLabels);

        final MockHttpServletResponse response = testUtils.perform(get(
                BASE_URL + LABEL_CONTROLLER_URL), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> actualLabels = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(actualLabels).hasSize(expectedLabels.size());
    }

    @Test
    public void getLabelByIdTest() throws Exception {

        final Label expectedLabel = labelRepository.save(Label.builder()
                .name("Test label")
                .build());

        final long id = expectedLabel.getId();
        final MockHttpServletRequestBuilder request = get(BASE_URL + LABEL_CONTROLLER_URL + "/" + id, id);
        final MockHttpServletResponse response = testUtils.perform(request, TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label actualLabel = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expectedLabel.getId(), actualLabel.getId());
        assertEquals(expectedLabel.getName(), actualLabel.getName());
    }

    @Test
    public void createLabelTest() throws Exception {
        createLabelForTest(labelDTO);
        assertFalse(labelRepository.findAll().isEmpty());
    }

    @Test
    public void updateLabelTest() throws Exception {

        final Label createdLabel = createLabelForTest(labelDTO);
        final long id = createdLabel.getId();

        final LabelDTO updatedLabelDto = new LabelDTO("Updated name of LabelDTO");

        final MockHttpServletRequestBuilder updatingRequest = put(
                BASE_URL + LABEL_CONTROLLER_URL + "/" + id, id)
                .content(asJson(updatedLabelDto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updatingRequest, TEST_USERNAME).andExpect(status().isOk());
        final Label updatedLabel = labelService.getLabelById(id);
        assertThat(updatedLabel.getName()).isEqualTo("Updated name of LabelDTO");
    }

    @Test
    public void deleteLabelTest() throws Exception {

        final Label createdLabel = createLabelForTest(labelDTO);
        final long id = createdLabel.getId();

        testUtils.perform(delete(BASE_URL + LABEL_CONTROLLER_URL + "/" + id, id), TEST_USERNAME)
                .andExpect(status().isOk());

        assertFalse(labelRepository.existsById(id));
    }

    private Label createLabelForTest(final LabelDTO labelDTO) throws Exception {

        final MockHttpServletRequestBuilder request = post(BASE_URL + LABEL_CONTROLLER_URL)
                .content(asJson(labelDTO))
                .contentType(MediaType.APPLICATION_JSON);

        final MockHttpServletResponse response = testUtils
                .perform(request, TEST_USERNAME).andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        return fromJson(response.getContentAsString(), new TypeReference<>() { });
    }
}

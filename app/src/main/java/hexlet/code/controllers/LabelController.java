package hexlet.code.controllers;

import hexlet.code.dto.LabelDTO;
import hexlet.code.models.Label;
import hexlet.code.services.LabelServiceImplementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.xml.bind.ValidationException;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static hexlet.code.controllers.LabelController.LABEL_CONTROLLER_URL;

@RestController
@Tag(name = "Label Controller")
@AllArgsConstructor
@RequestMapping(path = "${base-url}" + LABEL_CONTROLLER_URL)
public class LabelController {
    
    public static final String LABEL_CONTROLLER_URL = "/labels";
    
    private final LabelServiceImplementation labelService;
    
    @Operation(summary = "Get all labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels were successfully found and loaded",
            content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "404", description = "There are not labels stored in DB")
    })
    @GetMapping
    public List<Label> getAllLabels() {
        return labelService.getAllLabels();
    }
    
    @Operation(summary = "Create a new label")
    @ApiResponse(responseCode = "201", description = "A new label was successfully created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Label createLabel(@RequestBody @Valid final LabelDTO labelDTO) throws ValidationException {
        return labelService.createNewLabel(labelDTO);
    }
    
    @Operation(summary = "Update a particular label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated successfully"),
            @ApiResponse(responseCode = "404", description = "Label with such id not found")
    })
    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable("id") final Long id,
                             @RequestBody final LabelDTO labelDTO) {
        
        return labelService.updateLabel(id, labelDTO);
    }
    
    @Operation(summary = "Get a particular label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label was found"),
            @ApiResponse(responseCode = "404", description = "Label with such id does not exist")
    })
    @GetMapping("/{id}")
    public Label findLabelById(@PathVariable("id") final Long id) {
        return labelService.getLabelById(id);
    }
    
    @Operation(summary = "Delete label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label deleted"),
            @ApiResponse(responseCode = "404", description = "Label with such id is not found")
    })
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable("id") final Long id) {
        labelService.deleteLabelById(id);
    }
    
}

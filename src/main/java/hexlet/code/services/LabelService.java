package hexlet.code.services;

import hexlet.code.dto.LabelDTO;
import hexlet.code.models.Label;
import jakarta.xml.bind.ValidationException;

import java.util.List;

public interface LabelService {

    List<Label> getAllLabels();

    Label createNewLabel(LabelDTO labelDto) throws ValidationException;

    Label getLabelById(Long id);

    Label updateLabel(Long id, LabelDTO labelDto);

    void deleteLabelById(Long id);
}

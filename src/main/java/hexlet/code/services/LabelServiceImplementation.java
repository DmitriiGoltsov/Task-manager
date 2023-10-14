package hexlet.code.services;

import hexlet.code.dto.LabelDTO;
import hexlet.code.models.Label;
import hexlet.code.repositories.LabelRepository;
import jakarta.xml.bind.ValidationException;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImplementation implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label createNewLabel(LabelDTO labelDto) throws ValidationException {
        if (labelRepository.findLabelByName(labelDto.getName()).isPresent()) {
            throw new ValidationException("Label with name " + labelDto.getName() + " already exists");
        }

        final Label labelToAdd = new Label();
        labelToAdd.setName(labelDto.getName());

        return labelRepository.save(labelToAdd);
    }

    @Override
    public Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id " + id + " not found"));
    }

    @Override
    public Label updateLabel(Long id, LabelDTO labelDto) {
        Label labelToBeUpdated = getLabelById(id);

        Optional<String> newName = Optional.ofNullable(labelDto.getName());

        newName.ifPresent(labelToBeUpdated::setName);

        return labelRepository.save(labelToBeUpdated);
    }

    @Override
    public void deleteLabelById(Long id) {
        Label labelToBeRemoved = labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id " + id + " not found"));

        labelRepository.delete(labelToBeRemoved);
    }
}

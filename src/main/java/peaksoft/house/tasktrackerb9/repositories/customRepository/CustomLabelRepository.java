package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;

import java.util.List;

public interface CustomLabelRepository {
    List<LabelResponse> getAllLabels();
    List<LabelResponse> getAllLabelsByCardId(Long cardId);
    LabelResponse getLabelById(Long labelId);
}

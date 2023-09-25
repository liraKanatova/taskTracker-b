package peaksoft.house.tasktrackerb9.services;


import peaksoft.house.tasktrackerb9.dto.request.LabelRequest;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface LabelService {

    List<LabelResponse> getAllLabels();
    SimpleResponse saveLabels(LabelRequest labelRequest);
    SimpleResponse addLabelsToCard(Long cardId,Long labelId);

    LabelResponse getLabelById(Long labelId);

    SimpleResponse updateLabelDeleteById(Long labelId, LabelRequest labelRequest);

    SimpleResponse deleteFromCardByIdLables(Long cardId,Long labelId);

    List<LabelResponse> getAllLabelsByCardId(Long cardId);

}

package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.AttachmentResponse;

import java.util.List;

public interface CustomAttachmentRepository {

    List<AttachmentResponse> getAttachmentsByCardId(Long cardId);
}

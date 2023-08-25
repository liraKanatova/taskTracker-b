package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.AttachmentResponse;

public interface CustomAttachmentRepository {

    AttachmentResponse getAttachmentByCardId(Long cardId);
}

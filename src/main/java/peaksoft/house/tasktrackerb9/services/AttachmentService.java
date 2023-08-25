package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.AttachmentRequest;
import peaksoft.house.tasktrackerb9.dto.response.AttachmentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

public interface AttachmentService {

    AttachmentResponse saveAttachmentToCard(AttachmentRequest attachmentRequest);

    AttachmentResponse getAttachmentByCardId(Long cardId);

    SimpleResponse deleteAttachment(Long attachmentId);
}

package peaksoft.house.tasktrackerb9.dto.request;

import lombok.Builder;

@Builder
public record AttachmentRequest(
        String documentLink,
        Long cardId
) {
}

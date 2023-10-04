package peaksoft.house.tasktrackerb9.dto.request;

import lombok.Builder;

@Builder
public record BoardUpdateRequest(
        Long boardI,
        String title,
        String backGround
) {
}

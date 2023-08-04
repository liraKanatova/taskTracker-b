package peaksoft.house.tasktrackerb9.dto.request;

import lombok.Builder;

@Builder
public record ColumnRequest(Long boardId,String title) {
}

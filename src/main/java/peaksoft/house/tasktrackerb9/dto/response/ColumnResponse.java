package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ColumnResponse {

    private Long columnId;

    private String title;

    private Boolean isArchive;

    public ColumnResponse(Long columnId, String title, Boolean isArchive) {

        this.columnId = columnId;
        this.title = title;
        this.isArchive=isArchive;
    }
}

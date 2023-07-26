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

    private Long id;

    private String title;

    public ColumnResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}

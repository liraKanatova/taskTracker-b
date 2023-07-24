package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String backGround;
    private Boolean isFavorite;

}

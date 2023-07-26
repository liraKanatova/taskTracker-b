package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkSpaceFavoriteResponse {

    private Long workSpaceId;

    private String name;

    private boolean isFavorite;

}

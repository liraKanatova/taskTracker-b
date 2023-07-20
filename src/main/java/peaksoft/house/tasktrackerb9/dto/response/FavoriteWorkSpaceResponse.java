package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteWorkSpaceResponse {

    private Long id;

    private Long workSpaceId;

    private String name;

}

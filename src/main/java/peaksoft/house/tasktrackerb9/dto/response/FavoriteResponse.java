package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {

    private List<FavoriteBoardResponse> boardResponses;

    private List<FavoriteWorkSpaceResponse> workSpaceResponses;
}

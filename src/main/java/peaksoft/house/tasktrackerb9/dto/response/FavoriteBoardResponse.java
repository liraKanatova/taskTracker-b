package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteBoardResponse {

    private Long userId;

    private Long boardId;

    private String title;

    private String backGround;
}

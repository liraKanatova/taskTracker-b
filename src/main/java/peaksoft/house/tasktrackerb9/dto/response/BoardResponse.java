package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private Long boardId;

    private String title;

    private String backGround;

    private boolean isFavorite;

    public BoardResponse(Long boardId, String title, String backGround) {
        this.boardId = boardId;
        this.title = title;
        this.backGround = backGround;
    }
}

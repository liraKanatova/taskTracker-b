package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CardResponse {

    private Long cardId;
    private String title;

    public CardResponse(Long cardId, String title) {
        this.cardId = cardId;
        this.title = title;
    }
}

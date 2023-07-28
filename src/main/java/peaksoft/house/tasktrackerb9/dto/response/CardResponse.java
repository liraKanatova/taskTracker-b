package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponse {

    private Long cardId;
    private String title;
    private String duration;
    private int numberUsers;
    private int numberItems;
    private int numberCompletedItems;
    private List<LabelResponse> labelResponses;
    private List<CommentResponse> commentResponses;

}
package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllIssuesResponse {

    private Long cardId;
    private Date created;
    private String durationDay;
    private Long creatorId;
    private String creatorFullName;
    private String column;
    private List<UserAllIssuesResponse> assignee;
    private List<LabelResponse> labelResponses;
    private String checkListResponse;
    private String description;
    private int countOfCards;

}

package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String comment;
    private String createdDate;
}

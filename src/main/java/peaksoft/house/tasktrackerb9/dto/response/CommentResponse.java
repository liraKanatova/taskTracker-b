package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String comment;
    private String createdDate;
    private CommentUserResponse commentUserResponse;

}
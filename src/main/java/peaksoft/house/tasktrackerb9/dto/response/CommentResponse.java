package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String comment;
    private String createdDate;
    private CommentUserResponse commentUserResponse;

    public CommentResponse(Long commentId, String comment, String createdDate) {
        this.commentId = commentId;
        this.comment = comment;
        this.createdDate = createdDate;
    }
}
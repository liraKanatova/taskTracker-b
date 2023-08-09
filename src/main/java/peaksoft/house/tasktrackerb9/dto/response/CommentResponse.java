package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String comment;
    private String createdDate;
    private Long creatorId;
    private String creatorName;
    private String creatorAvatar;

}

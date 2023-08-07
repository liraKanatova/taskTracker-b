package peaksoft.house.tasktrackerb9.dto.response;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
=======
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponse {

>>>>>>> 8344fca9e05c8606ffd08184de646cd94c153015
    private Long commentId;
    private String comment;
    private String createdDate;
    private Long creatorId;
    private String creatorName;
    private String creatorAvatar;

<<<<<<< HEAD

=======
>>>>>>> 8344fca9e05c8606ffd08184de646cd94c153015
}

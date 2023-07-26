package peaksoft.house.tasktrackerb9.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment>getCommentByUserIdAndId(Long userId,Long commentId);

}

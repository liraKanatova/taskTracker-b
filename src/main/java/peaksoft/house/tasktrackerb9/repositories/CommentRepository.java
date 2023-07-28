package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
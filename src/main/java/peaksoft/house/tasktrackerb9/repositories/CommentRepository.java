package peaksoft.house.tasktrackerb9.repositories;

<<<<<<< HEAD

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {


    Optional<Comment> getCommentById(Long commentId);
}
=======
import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
>>>>>>> 8344fca9e05c8606ffd08184de646cd94c153015

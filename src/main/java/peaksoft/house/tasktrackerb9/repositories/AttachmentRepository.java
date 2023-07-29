package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

    @Query("select a from Attachment a where a.card.id = :cardId")
    List<Attachment> getAllAttachmentsByCardId(Long cardId);
}

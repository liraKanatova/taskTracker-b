package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

}

package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.Estimation;

public interface EstimationRepository extends JpaRepository<Estimation,Long> {

    @Query("select e from Estimation e where e.card.id = :cardId")
    Estimation findEstimationByCardId(Long cardId);

}
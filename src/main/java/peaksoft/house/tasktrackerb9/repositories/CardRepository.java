package peaksoft.house.tasktrackerb9.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.WorkSpace;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {

    @Query("SELECT ws FROM WorkSpace ws JOIN ws.boards b JOIN b.columns c JOIN c.cards ca WHERE ca.id = ?1")
    Optional<WorkSpace> getWorkSpaceByCardId(Long cardId);

    @Query("SELECT ws.adminId FROM Card c JOIN c.column co JOIN co.board b JOIN b.workSpace ws WHERE c.id = ?1")
    Optional<Long> getUserIdByCardId(Long cardId);

    @Query("SELECT u.id FROM User u JOIN u.cards c WHERE c.id = ?1")
    List<Long> getMembersByCardId(Long cardId);

}
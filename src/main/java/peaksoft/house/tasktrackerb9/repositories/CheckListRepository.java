package peaksoft.house.tasktrackerb9.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.CheckList;

import java.util.List;

public interface CheckListRepository extends JpaRepository<CheckList,Long> {

    @Query("select c from CheckList c where c.card.id = :cardId")
    List<CheckList> findAllCheckListByCardId(Long cardId);

}

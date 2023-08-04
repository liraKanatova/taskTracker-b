package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.CheckList;

public interface CheckListRepository extends JpaRepository<CheckList,Long> {

}

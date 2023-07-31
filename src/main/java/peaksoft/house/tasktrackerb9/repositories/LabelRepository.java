package peaksoft.house.tasktrackerb9.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Label;

public interface LabelRepository extends JpaRepository<Label,Long> {

}

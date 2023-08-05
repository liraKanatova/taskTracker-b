package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Column;

public interface ColumnRepository  extends JpaRepository<Column,Long> {

}
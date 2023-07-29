package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.models.Label;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label,Long> {

    @Query("select new peaksoft.house.tasktrackerb9.dto.response.LabelResponse(l.id,l.labelName,l.color) from Label l")
    List<LabelResponse> getAllLabelResponse();
}
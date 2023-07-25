package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.WorkSpace;


import java.util.Optional;

public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long> {
    Optional<WorkSpace> getWorkSpaceByAdminIdAndId(Long adminId, Long id);


}

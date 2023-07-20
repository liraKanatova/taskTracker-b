package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.entity.UserWorkSpaceRole;

public interface UserWorkSpaceRoleRepository extends JpaRepository<UserWorkSpaceRole, Long> {

    @Query("select u from UserWorkSpaceRole u join u.user where u.user.id=:userId and u.workSpace.id=:workSpaceId")
    UserWorkSpaceRole getUserIdAndWorkSpaceId(Long userId, Long workSpaceId);
}
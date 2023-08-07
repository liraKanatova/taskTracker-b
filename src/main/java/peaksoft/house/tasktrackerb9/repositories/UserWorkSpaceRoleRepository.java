package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.UserWorkSpaceRole;

public interface UserWorkSpaceRoleRepository extends JpaRepository<UserWorkSpaceRole,Long> {

    @Query("select u from UserWorkSpaceRole u where u.member.id = :userId and u.workSpace.id = :workSpaceId")
    UserWorkSpaceRole findByUserIdAndWorkSpacesId(Long userId,Long workSpaceId);

}
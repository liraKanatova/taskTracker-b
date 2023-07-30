package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select new peaksoft.house.tasktrackerb9.dto.response.MemberResponse(u.id,u.firstName,u.lastName,u.email,u.image,u.role)from User u join UserWorkSpaceRole uw on uw.workSpace.id = :workSpaceId and uw.user.id=u.id where lower( concat(u.firstName,u.lastName,u.email) )LIKE lower( concat('%',:word,'%') )")
    List<MemberResponse> searchByWord(Long workSpaceId, String word);
}

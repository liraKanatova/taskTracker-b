package peaksoft.house.tasktrackerb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

}

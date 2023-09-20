package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByCard(Card card);


    @Query("select n from Notification n join n.members m where m.id = :id")
    List<Notification> getAllNotification(Long id);
    @Query("select n from Notification n join n.members m where m.id = :id and n.id = :userId")
    Optional<Notification> getNotificationByIdUser(Long id, Long userId);

}
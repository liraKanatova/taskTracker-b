package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.models.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("select n from Notification n where n.estimation.id = :id")
    Notification findNotificationByEstimationId(Long id);

    @Query("select e from Estimation e where e.notification.id = :id")
    Estimation findEstimationByNotificationId(Long id);

    List<Notification> findByCard(Card card);
}
package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("select n from Notification n where n.estimation.id = :id")
    Notification findNotificationByEstimationId(Long id);

    @Query("select e from Estimation e where e.notification.id = :id")
    Estimation findEstimationByNotificationId(Long id);

}
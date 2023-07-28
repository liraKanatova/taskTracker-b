package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByCard(Card card);
}

package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;
import peaksoft.house.tasktrackerb9.models.User;

import java.util.List;

public interface CustomNotificationRepository {

    List<NotificationResponse> getAllNotifications();

    NotificationResponse getNotificationById(Long notificationId);


}
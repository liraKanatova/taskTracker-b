package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;

import java.util.List;

public interface CustomNotificationRepository {

    List<NotificationResponse> getAllNotifications();

    NotificationResponse getNotificationById(Long notificationId);

    List<NotificationResponse> markAsRead();

}
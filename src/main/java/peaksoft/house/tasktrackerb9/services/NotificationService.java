package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getAllNotifications();

    NotificationResponse getNotificationById(Long notificationId);

    SimpleResponse markAsRead();


}
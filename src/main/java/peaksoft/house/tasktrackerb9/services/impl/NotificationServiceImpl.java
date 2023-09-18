package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Notification;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.NotificationRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomNotificationRepository;
import peaksoft.house.tasktrackerb9.services.NotificationService;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomNotificationRepository customNotificationRepository;
    private final JwtService jwtService;

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return customNotificationRepository.getAllNotifications();
    }

    @Override
    public NotificationResponse getNotificationById(Long notificationId) {
        notificationRepository.findById(notificationId).orElseThrow(() -> {
            log.error("Notification with id: " + notificationId + " not found!");
            return new NotFoundException("Notification with id: " + notificationId + " not found!");
        });
        return customNotificationRepository.getNotificationById(notificationId);
    }

    @Override
    public SimpleResponse markAsRead() {
        User user = jwtService.getAuthentication();
        List<Notification> notifications = notificationRepository.getAllNotification(user.getId());
        for (Notification notification : notifications) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(" Mark as read successfully 👍")
                .build();
    }

}

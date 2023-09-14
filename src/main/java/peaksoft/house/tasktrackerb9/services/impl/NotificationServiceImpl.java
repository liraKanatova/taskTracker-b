package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
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
    public List<NotificationResponse> markAsRead() {
      return customNotificationRepository.markAsRead();
    }
}

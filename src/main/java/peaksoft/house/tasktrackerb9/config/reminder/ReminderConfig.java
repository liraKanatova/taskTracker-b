package peaksoft.house.tasktrackerb9.config.reminder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import peaksoft.house.tasktrackerb9.enums.NotificationType;
import peaksoft.house.tasktrackerb9.enums.ReminderType;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.models.Notification;
import peaksoft.house.tasktrackerb9.repositories.EstimationRepository;
import peaksoft.house.tasktrackerb9.repositories.NotificationRepository;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Configuration
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ReminderConfig {

    private final EstimationRepository estimationRepository;
    private final NotificationRepository notificationRepository;
    private static final ZoneId ALMATY_ZONE = ZoneId.of("Asia/Almaty");

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void reminder() {
        log.info("Current time in Almaty: {}", ZonedDateTime.now(ALMATY_ZONE));
        List<Estimation> estimations = estimationRepository.findAll();
        if (!estimations.isEmpty()) {
            ZonedDateTime now = ZonedDateTime.now(ALMATY_ZONE);
            for (Estimation estimation : estimations) {
                ReminderType reminderType = estimation.getReminderType();
                ZonedDateTime estimatedNotificationTime = estimation.getNotificationTime();
                Duration timeDifference = Duration.between(now, estimatedNotificationTime);
                long minutesUntilNotification = timeDifference.toMinutes();

                switch (reminderType) {
                    case FIVE_MINUTE ->
                            checkAndCreateNotification(estimation, minutesUntilNotification, 5, "There are 5 minutes left until the end of time!");
                    case TEN_MINUTE ->
                            checkAndCreateNotification(estimation, minutesUntilNotification, 10, "There are 10 minutes left until the end of time!");
                    case FIFTEEN_MINUTE ->
                            checkAndCreateNotification(estimation, minutesUntilNotification, 15, "There are 15 minutes left until the end of time!");
                    case THIRD_MINUTE ->
                            checkAndCreateNotification(estimation, minutesUntilNotification, 30, "There are 30 minutes left until the end of time!");
                    default -> {
                    }
                }
            }
        }
    }

    private void checkAndCreateNotification(Estimation estimation, long minutesUntilNotification, int targetMinutes, String message) {
        if (minutesUntilNotification == targetMinutes) {
            createAndSaveNotification(estimation, message);
        }
    }

    private void createAndSaveNotification(Estimation estimation, String message) {
        Notification notification = new Notification();
        Card card = estimation.getCard();
        notification.setCard(card);
        notification.setType(NotificationType.REMINDER);
        notification.setFromUserId(card.getCreatorId());
        notification.setBoardId(card.getColumn().getBoard().getId());
        notification.setColumnId(card.getColumn().getId());
        notification.setEstimation(estimation);
        notification.setMembers(card.getMembers());
        notification.setCreatedDate(ZonedDateTime.now(ALMATY_ZONE));
        notification.setIsRead(false);
        notification.setText(message);
        log.info(message);
        notificationRepository.save(notification);
    }

}

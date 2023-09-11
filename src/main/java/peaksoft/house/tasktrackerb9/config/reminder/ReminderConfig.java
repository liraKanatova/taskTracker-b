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
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Scanner;

@Configuration
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ReminderConfig {

    private final EstimationRepository estimationRepository;
    private final NotificationRepository notificationRepository;
    private static final ZoneId BISHKEK_ZONE = ZoneId.of("Asia/Bishkek");

    @Transactional
    @Scheduled(cron = "0 0/1 * * * *")
    public void reminder() {

        log.warn("in reminder");

        List<Estimation> estimations = estimationRepository.findAll();
        if (!estimations.isEmpty()) {
            log.warn("in first if");
            for (Estimation estimation : estimations) {
//                long minutesUntilNotification = Duration.between(ZonedDateTime.now(BISHKEK_ZONE), estimation.getNotificationTime()).toMinutes();

                log.warn("in for");
                ZonedDateTime now = ZonedDateTime.now(BISHKEK_ZONE)
                        .with(ChronoField.MILLI_OF_SECOND, 0)
                        .with(ChronoField.SECOND_OF_MINUTE, 0);

                if (estimation.getNotificationTime() != null) {
                    log.warn("notification time not null");
                    if (estimation.getNotificationTime().equals(now)) {

                        log.warn("in second if, times is equals");
                        Notification notification = new Notification();
                        Card card = estimation.getCard();
                        notification.setCard(card);
                        notification.setType(NotificationType.REMINDER);
                        notification.setFromUserId(card.getCreatorId());
                        notification.setBoardId(card.getColumn().getBoard().getId());
                        notification.setColumnId(card.getColumn().getId());
                        notification.setEstimation(estimation);
                        notification.setMembers(card.getMembers());
                        notification.setCreatedDate(ZonedDateTime.now(BISHKEK_ZONE));
                        notification.setIsRead(false);

                        if (estimation.getReminderType().equals(ReminderType.FIVE_MINUTE)) {
                            notification.setText(estimation.getCard().getTitle() + ": timeout expires in 5 minutes!");
                        } else if (estimation.getReminderType().equals(ReminderType.TEN_MINUTE)) {
                            notification.setText(estimation.getCard().getTitle() + ": timeout expires in 10 minutes!");
                        } else if (estimation.getReminderType().equals(ReminderType.FIFTEEN_MINUTE)) {
                            notification.setText(estimation.getCard().getTitle() + ": timeout expires in 15 minutes!");
                        } else if (estimation.getReminderType().equals(ReminderType.THIRD_MINUTE)) {
                            notification.setText(estimation.getCard().getTitle() + ": timeout expires in 30 minutes!");
                        }
                        notificationRepository.save(notification);
                        log.warn("after save notification");
                    }
                }
            }
        }
    }
}

//    private void checkAndCreateNotification(Estimation estimation, String message) {
//        Notification notification = new Notification();
//        Card card = estimation.getCard();
//        notification.setCard(card);
//        notification.setType(NotificationType.REMINDER);
//        notification.setFromUserId(card.getCreatorId());
//        notification.setBoardId(card.getColumn().getBoard().getId());
//        notification.setColumnId(card.getColumn().getId());
//        notification.setEstimation(estimation);
//        notification.setMembers(card.getMembers());
//        notification.setCreatedDate(ZonedDateTime.now(BISHKEK_ZONE));
//        notification.setIsRead(false);
//        notification.setText(message);
//        log.info(message);
//        notificationRepository.save(notification);
//    }
//}
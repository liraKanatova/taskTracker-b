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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        List<Estimation> estimations = estimationRepository.findAll();

        if (!estimations.isEmpty()) {

            for (Estimation estimation : estimations) {

                if (!estimation.getReminderType().equals(ReminderType.NONE)) {

                    if (estimation.getNotificationTime().format(formatter).equals(ZonedDateTime.now(BISHKEK_ZONE).format(formatter))) {

                        Notification notification = new Notification();
                        Card card = estimation.getCard();
                        notification.setCard(card);
                        notification.setType(NotificationType.REMINDER);
                        notification.setFromUserId(card.getCreatorId());
                        notification.setBoardId(card.getColumn().getBoard().getId());
                        notification.setColumnId(card.getColumn().getId());
                        notification.setEstimation(estimation);
                        notification.addMembers(card.getMembers());
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
                        estimation.setNotification(notification);
                    }
                }
            }
        }
    }
}
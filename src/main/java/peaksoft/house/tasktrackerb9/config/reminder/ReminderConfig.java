package peaksoft.house.tasktrackerb9.config.reminder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.repositories.EstimationRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

@Configuration
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ReminderConfig {

    private final EstimationRepository estimationRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void reminder() {
        Scanner scanner =new Scanner(System.in);
        ZonedDateTime e = ZonedDateTime.now();
        List<Estimation> estimations = estimationRepository.findAll();
        for (Estimation estimation : estimations) {
            if (estimation.getNotificationTime().equals(e))
                estimation.setTime(ZonedDateTime.parse(scanner.nextLine()));
                System.out.println(estimation.getTime());
        }
    }
}



package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.EstimationRequest;
import peaksoft.house.tasktrackerb9.dto.response.EstimationResponse;
import peaksoft.house.tasktrackerb9.enums.ReminderType;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.BadRequestException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.EstimationRepository;
import peaksoft.house.tasktrackerb9.services.EstimationService;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EstimationServiceImpl implements EstimationService {

    private final EstimationRepository estimationRepository;

    private final CardRepository cardRepository;


    @Override
    public EstimationResponse createdEstimation(EstimationRequest request) {
        Estimation estimation = new Estimation();
        Card card = cardRepository.findById(request.getCardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.getCardId() + " not found");
            return new NotFoundException("Card with id: " + request.getCardId() + " not found");
        });

        if (card.getEstimation() == null) {
            if (request.startDate().isBefore(request.dateOfFinish())) {
                estimation.setStartDate(request.startDate());
                estimation.setFinishDate(request.dateOfFinish());
                estimation.setStartTime(request.startTime());
                estimation.setFinishTime(request.finishTime());
                if (request.reminder().equals("NONE")) {
                    estimation.setReminderType(ReminderType.NONE);
                }
                if (request.reminder().equals("5")) {
                    estimation.setReminderType(ReminderType.FIVE_MINUTE);
                    if (estimation.getFinishTime() != null) {
                        estimation.setNotificationTime(estimation
                                .getFinishTime()
                                .minusMinutes(5)
                                .with(ChronoField.MILLI_OF_SECOND, 0)
                                .with(ChronoField.SECOND_OF_MINUTE, 0));
                    } else throw new BadCredentialException("Notification finish time must be not null");
                }
                if (request.reminder().equals("10")) {
                    estimation.setReminderType(ReminderType.TEN_MINUTE);
                    if (estimation.getFinishTime() != null) {
                        estimation.setNotificationTime(estimation
                                .getFinishTime()
                                .minusMinutes(10)
                                .with(ChronoField.MILLI_OF_SECOND, 0)
                                .with(ChronoField.SECOND_OF_MINUTE, 0));
                    } else throw new BadCredentialException("Notification finish time must be not null");
                }

                if (request.reminder().equals("15")) {
                    estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
                    if (estimation.getFinishTime() != null) {
                        estimation.setNotificationTime(estimation
                                .getFinishTime()
                                .minusMinutes(15)
                                .with(ChronoField.MILLI_OF_SECOND, 0)
                                .with(ChronoField.SECOND_OF_MINUTE, 0));
                    } else throw new BadCredentialException("Notification finish time must be not null");
                }

                if (request.reminder().equals("30")) {
                    estimation.setReminderType(ReminderType.THIRD_MINUTE);
                    if (estimation.getFinishTime() != null) {
                        estimation.setNotificationTime(estimation
                                .getFinishTime()
                                .minusMinutes(30)
                                .with(ChronoField.MILLI_OF_SECOND, 0)
                                .with(ChronoField.SECOND_OF_MINUTE, 0));
                    } else throw new BadCredentialException("Notification finish time must be not null");
                }

                card.setEstimation(estimation);
                estimationRepository.save(estimation);
                log.info("Successfully saved estimation: " + estimation);
            } else throw new BadRequestException("The start date must not be before the finish date");
        } else throw new BadRequestException("This card already has an estimation");
        return EstimationResponse.builder().
                estimationId(estimation.getId()).
                startDate(estimation.getStartDate().toString()).
                duetDate(estimation.getFinishDate().toString()).
                finishTime(estimation.getFinishTime().toString()).
                reminderType(estimation.getReminderType()).
                build();
    }

    @Override
    public EstimationResponse updateEstimation(EstimationRequest request) {
        Estimation estimation = estimationRepository.findById(request.getCardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.getCardId() + "  not found");
            return new NotFoundException("Card with id: " + request.getCardId() + " id not found");
        });
        if (request.getStartDate() != null || !request.getStartDate().toString().isEmpty()) {
            estimation.setStartDate(request.getStartDate());
        } else {
            estimation.setStartDate(estimation.getStartDate());
        }
        if (request.getDateOfFinish() != null || !request.getDateOfFinish().toString().isEmpty()) {
            estimation.setFinishDate(request.getDateOfFinish());
        } else {
            estimation.setFinishDate(estimation.getFinishDate());
        }
        if (request.startTime() != null || !request.startTime().toString().isEmpty()) {
            estimation.setFinishTime(request.finishTime());
        } else {
            estimation.setFinishTime(estimation.getFinishTime());
        }
        if (request.finishTime() != null || !request.finishTime().toString().isEmpty()) {
            estimation.setFinishTime(request.finishTime());
        } else {
            estimation.setFinishTime(estimation.getFinishTime());
        }
        if (request.getReminder().equals("NONE")) {
            estimation.setReminderType(ReminderType.NONE);
        }
        if (request.getReminder().equals("5")) {
            estimation.setReminderType(ReminderType.FIVE_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(5)
                        .with(ChronoField.MILLI_OF_SECOND, 0)
                        .with(ChronoField.SECOND_OF_MINUTE, 0));
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        if (request.getReminder().equals("10")) {
            estimation.setReminderType(ReminderType.TEN_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(10)
                        .with(ChronoField.MILLI_OF_SECOND, 0)
                        .with(ChronoField.SECOND_OF_MINUTE, 0)
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        if (request.getReminder().equals("15")) {
            estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(15)
                        .with(ChronoField.MILLI_OF_SECOND, 0)
                        .with(ChronoField.SECOND_OF_MINUTE, 0)
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        if (request.getReminder().equals("30")) {
            estimation.setReminderType(ReminderType.THIRD_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(5)
                        .with(ChronoField.MILLI_OF_SECOND, 0)
                        .with(ChronoField.SECOND_OF_MINUTE, 0)
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        estimationRepository.save(estimation);
        log.info("Successfully estimation updated!");
        return EstimationResponse.builder().
                estimationId(estimation.getId()).
                startDate(estimation.getStartDate().toString()).
                duetDate(estimation.getFinishDate().toString()).
                finishTime(estimation.getFinishTime().toString()).
                reminderType(estimation.getReminderType()).
                build();

    }
}
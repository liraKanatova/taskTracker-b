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
        Card card = cardRepository.findById(request.cardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.cardId() + " id not found");
            return new NotFoundException("Card with id: " + request.cardId() + "  not found");
        });
        if (card.getEstimation() == null) {
            log.info("start time: " + request.startTime().toLocalTime());
            if (!request.startDate().toLocalDate().isAfter(request.dateOfFinish().toLocalDate())) {
                if (request.startDate().toLocalDate().equals(request.dateOfFinish().toLocalDate())) {
                    if (request.startTime().toLocalTime().equals(request.finishTime().toLocalTime())) {
                        throw new BadRequestException("Date of start and date finish and time of start and time finish can't be equals!");
                    }
                }
                if (!request.finishTime().toLocalTime().isBefore(request.startTime().toLocalTime())) {
                    estimation.setStartDate(request.startDate());
                    estimation.setFinishDate(request.dateOfFinish());
                    estimation.setStartTime(request.startTime());
                    estimation.setTime(request.finishTime());
                    if (request.reminder().equals("NONE")) {
                        estimation.setReminderType(ReminderType.NONE);
                    }
                    if (request.reminder().equals("5")) {
                        estimation.setReminderType(ReminderType.FIVE_MINUTE);
                        if (estimation.getTime() != null) {
                            estimation.setNotificationTime(estimation.getTime().minusMinutes(5));
                        } else throw new BadCredentialException("Notification finish time must be not null");
                    }
                    if (request.reminder().equals("10")) {
                        estimation.setReminderType(ReminderType.TEN_MINUTE);
                        if (estimation.getTime() != null) {
                            estimation.setNotificationTime(estimation.getTime().minusMinutes(10));
                        } else throw new BadCredentialException("Notification finish time must be not null");

                    }
                    if (request.reminder().equals("15")) {
                        estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
                        if (estimation.getTime() != null) {
                            estimation.setNotificationTime(estimation.getTime().minusMinutes(15));
                        } else throw new BadCredentialException("Notification finish time must be not null");
                    }
                    if (request.reminder().equals("30")) {
                        estimation.setReminderType(ReminderType.THIRD_MINUTE);
                        if (estimation.getTime() != null) {
                            estimation.setNotificationTime(estimation.getTime().minusMinutes(30));
                        } else throw new BadCredentialException("Notification finish time must be not null");
                    }
                    estimation.setCard(card);
                    estimationRepository.save(estimation);
                    card.setEstimation(estimation);
                    log.info("Successfully saved estimation: " + estimation);
                } else
                    throw new BadRequestException("The finish time can't be before then start time!");
            } else throw new BadRequestException("Finish date can't be before then start date");
        } else throw new BadRequestException("This card already has an estimation");
        return EstimationResponse.builder().
                estimationId(estimation.getId()).
                startDate(estimation.getStartDate().toString()).
                duetDate(estimation.getFinishDate().toString()).
                finishTime(estimation.getTime().toString()).
                reminderType(estimation.getReminderType()).
                build();
    }

    @Override
    public EstimationResponse updateEstimation(EstimationRequest request) {
        Estimation estimation = estimationRepository.findById(request.cardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.cardId() + "  not found");
            return new NotFoundException("Card with id: " + request.cardId() + " id not found");
        });
        if (request.startDate() != null || !request.startDate().toString().isEmpty()) {
            estimation.setStartDate(request.startDate());
        } else {
            estimation.setStartDate(estimation.getStartDate());
        }
        if (request.dateOfFinish() != null || !request.dateOfFinish().toString().isEmpty()) {
            estimation.setFinishDate(request.dateOfFinish());
        } else {
            estimation.setFinishDate(estimation.getFinishDate());
        }
        if (request.startTime() != null || !request.startTime().toString().isEmpty()) {
            estimation.setTime(request.finishTime());
        } else {
            estimation.setTime(estimation.getTime());
        }
        if (request.finishTime() != null || !request.finishTime().toString().isEmpty()) {
            estimation.setTime(request.finishTime());
        } else {
            estimation.setTime(estimation.getTime());
        }
        if (request.reminder().equals("NONE")) {
            estimation.setReminderType(ReminderType.NONE);
        }
        if (request.reminder().equals("5")) {
            estimation.setReminderType(ReminderType.FIVE_MINUTE);
            if (estimation.getTime() != null) {
                estimation.setNotificationTime(estimation.getTime().minusMinutes(5));
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        if (request.reminder().equals("10")) {
            estimation.setReminderType(ReminderType.TEN_MINUTE);
            if (estimation.getTime() != null) {
                estimation.setNotificationTime(estimation.getTime().minusMinutes(10));
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        if (request.reminder().equals("15")) {
            estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
            if (estimation.getTime() != null) {
                estimation.setNotificationTime(estimation.getTime().minusMinutes(15));
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        if (request.reminder().equals("30")) {
            estimation.setReminderType(ReminderType.THIRD_MINUTE);
            if (estimation.getTime() != null) {
                estimation.setNotificationTime(estimation.getTime().minusMinutes(5));
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        estimationRepository.save(estimation);
        log.info("Successfully estimation updated!");
        return EstimationResponse.builder().
                estimationId(estimation.getId()).
                startDate(estimation.getStartDate().toString()).
                duetDate(estimation.getFinishDate().toString()).
                finishTime(estimation.getTime().toString()).
                reminderType(estimation.getReminderType()).
                build();
    }
}

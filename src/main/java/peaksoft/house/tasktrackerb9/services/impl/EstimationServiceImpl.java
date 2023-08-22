package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.EstimationRequest;
import peaksoft.house.tasktrackerb9.dto.response.EstimationResponse;
import peaksoft.house.tasktrackerb9.enums.ReminderType;
import peaksoft.house.tasktrackerb9.exceptions.BadRequestException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.EstimationRepository;
import peaksoft.house.tasktrackerb9.services.EstimationService;

import java.time.ZonedDateTime;

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
        ZonedDateTime timer=request.dateOfFinish();
        Card card = cardRepository.findById(request.cardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.cardId() + " id not found");
            return new NotFoundException("Card with id: " + request.cardId() + "  not found");
        });
        if (card.getEstimation() == null) {
            if (request.startDate().isBefore(request.dateOfFinish())) {
                estimation.setStartDate(request.startDate());
                estimation.setFinishDate(request.dateOfFinish());
                String reminder = request.reminder();
                if ("None".equals(reminder)) {
                    estimation.setReminderType(ReminderType.NONE);
                } else if ("5".equals(reminder)) {
                    estimation.setReminderType(ReminderType.FIVE_MINUTE);
                } else if ("10".equals(reminder)) {
                    estimation.setReminderType(ReminderType.TEN_MINUTE);
                } else if ("15".equals(reminder)) {
                    estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
                } else if ("30".equals(reminder)) {
                    estimation.setReminderType(ReminderType.THIRD_MINUTE);
                } else {
                    throw new BadRequestException("Invalid reminder value");
                }
                ZonedDateTime time = timer.minusMinutes(estimation.getReminderType().getMinute());
                estimation.setTime(time);
                card.setEstimation(estimation);
                estimationRepository.save(estimation);
                log.info("Successfully saved estimation: " + estimation);
            } else {
                throw new BadRequestException("The start date must not be before the finish date");
            }
        } else {
            throw new BadRequestException("This card already has an estimation");
        }
        return EstimationResponse.builder()
                .estimationId(estimation.getId())
                .startDate(estimation.getStartDate().toString())
                .duetDate(estimation.getFinishDate().toString())
                .finishTime(estimation.getTime().toString())
                .reminderType(estimation.getReminderType())
                .build();
    }

    @Override
    public EstimationResponse updateEstimation(EstimationRequest request) {
        ZonedDateTime timer=request.dateOfFinish();
        Estimation estimation = estimationRepository.findById(request.cardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.cardId() + "  not found");
            return new NotFoundException("Card with id: " + request.cardId() + " id not found");
        });
        estimation.setStartDate(request.startDate());
        estimation.setFinishDate(request.dateOfFinish());
        String reminder = request.reminder();
        if ("None".equals(reminder)) {
            estimation.setReminderType(ReminderType.NONE);
        } else if ("5".equals(reminder)) {
            estimation.setReminderType(ReminderType.FIVE_MINUTE);
        } else if ("10".equals(reminder)) {
            estimation.setReminderType(ReminderType.TEN_MINUTE);
        } else if ("15".equals(reminder)) {
            estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
        } else if ("30".equals(reminder)) {
            estimation.setReminderType(ReminderType.THIRD_MINUTE);
        } else {
            throw new BadRequestException("Invalid reminder value");
        }
        ZonedDateTime time = timer.minusMinutes(estimation.getReminderType().getMinute());
        estimation.setTime(time);
        estimationRepository.save(estimation);
        log.info("Successfully estimation updated!");
        return EstimationResponse.builder()
                .estimationId(estimation.getId())
                .startDate(estimation.getStartDate().toString())
                .duetDate(estimation.getFinishDate().toString())
                .finishTime(estimation.getTime().toString())
                .reminderType(estimation.getReminderType())
                .build();
    }
}

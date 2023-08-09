package peaksoft.house.tasktrackerb9.services.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.EstimationRequest;
import peaksoft.house.tasktrackerb9.dto.response.EstimationResponse;
import peaksoft.house.tasktrackerb9.enums.ReminderType;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.BadRequestException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Estimation;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.EstimationRepository;
import peaksoft.house.tasktrackerb9.services.EstimationService;

import java.time.ZonedDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EstimationServiceImpl implements EstimationService {

    private final JwtService jwtService;

    private final EstimationRepository estimationRepository;

    private final CardRepository cardRepository;

    @Override
    public EstimationResponse createdEstimation(EstimationRequest request) {

        User user = jwtService.getAuthentication();
        Estimation estimation = new Estimation();
        Card card = cardRepository.findById(request.cardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.cardId() + " not found");
            throw new NotFoundException("Card with id: " + request.cardId() + " not found");
        });

        if (!user.getCards().contains(card)) {
            throw new BadCredentialException("This is not your card");
        }

        if (card.getEstimation() == null) {
            if (request.startDate().isBefore(request.dateOfFinish())) {
                estimation.setStartDate(request.startDate());
                estimation.setDuetDate(request.dateOfFinish());

                if (request.reminder().equals("None")) {
                    estimation.setReminderType(ReminderType.NONE);
                } else if (request.reminder().equals("5")) {
                    estimation.setReminderType(ReminderType.FIVE_MINUTE);
                } else if (request.reminder().equals("10")) {
                    estimation.setReminderType(ReminderType.TEN_MINUTE);
                } else if (request.reminder().equals("15")) {
                    estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
                } else if (request.reminder().equals("30")) {
                    estimation.setReminderType(ReminderType.THIRD_MINUTE);
                } else {
                    throw new BadRequestException("Invalid reminder value");
                }

                ZonedDateTime notificationTime = ZonedDateTime.now().minusMinutes(estimation.getReminderType().getMinutes());
                estimation.setTime(notificationTime);

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
                .duetDate(estimation.getDuetDate().toString())
                .finishTime(estimation.getTime().toString())
                .reminderType(estimation.getReminderType())
                .build();
    }


    @Override
    public EstimationResponse updateEstimation(EstimationRequest request) {
        User user = jwtService.getAuthentication();
        Card card = cardRepository.findById(request.cardId()).orElseThrow(() -> {
            log.info("Card with id: " + request.cardId() + " id not found");
            return new NotFoundException("Card with id: " + request.cardId() + " id not found");
        });
        if (!user.getCards().contains(card)) {
            throw new BadCredentialException("this is not your card");
        }
        if (card.getEstimation() == null) {
            throw new BadRequestException("This card already has estimation!");
        } else {
            card.getEstimation().setStartDate(request.startDate());
            card.getEstimation().setDuetDate(request.dateOfFinish());
            card.getEstimation().setReminderType(ReminderType.valueOf(request.reminder()));
            cardRepository.save(card);
            log.info("Successfully estimation updated!");
        }
        return EstimationResponse.builder()
                .estimationId(card.getEstimation().getId())
                .startDate(card.getEstimation().getStartDate().toString())
                .duetDate(card.getEstimation().getDuetDate().toString())
                .reminderType(card.getEstimation().getReminderType())
                .build();
    }
}

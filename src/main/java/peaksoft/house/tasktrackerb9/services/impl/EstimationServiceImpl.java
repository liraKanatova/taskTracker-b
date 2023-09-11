package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.EstimationRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateEstimationRequest;
import peaksoft.house.tasktrackerb9.dto.response.EstimationResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
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
                }
        );

        if (card.getEstimation() == null) {
            log.info("start time: " + request.getStartTime().toLocalTime());
            if (!request.getStartDate().toLocalDate().isAfter(request.getDateOfFinish().toLocalDate())) {

                if (request.getStartDate().toLocalDate().equals(request.getDateOfFinish().toLocalDate())) {

                    if (request.getStartTime().toLocalTime().equals(request.getFinishTime().toLocalTime())) {

                        throw new BadRequestException("Date of start and date finish and time of start and time finish can't be equal!");
                    }
                }

                if (!request.getFinishTime().toLocalTime().isBefore(request.getStartTime().toLocalTime())) {
                    estimation.setStartDate(request.getStartDate());
                    estimation.setFinishDate(request.getDateOfFinish());
                    estimation.setStartTime(request.getStartTime());
                    estimation.setFinishTime(request.getFinishTime());

                    if ("NONE".equals(request.getReminder())) {
                        estimation.setReminderType(ReminderType.NONE);

                    } else if ("5".equals(request.getReminder())) {
                        estimation.setReminderType(ReminderType.FIVE_MINUTE);

                        if (estimation.getFinishTime() != null) {
                            estimation.setNotificationTime(estimation
                                    .getFinishTime()
                                    .minusMinutes(5)
                            );

                        } else {
                            throw new BadCredentialException("Notification finish time must not be null");
                        }

                    } else if ("10".equals(request.getReminder())) {
                        estimation.setReminderType(ReminderType.TEN_MINUTE);

                        if (estimation.getFinishTime() != null) {
                            estimation.setNotificationTime(estimation
                                    .getFinishTime()
                                    .minusMinutes(10)
                            );

                        } else {
                            throw new BadCredentialException("Notification finish time must not be null");
                        }

                    } else if ("15".equals(request.getReminder())) {
                        estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);

                        if (estimation.getFinishTime() != null) {
                            estimation.setNotificationTime(estimation
                                    .getFinishTime()
                                    .minusMinutes(15)
                            );

                        } else {
                            throw new BadCredentialException("Notification finish time must not be null");
                        }

                    } else if ("30".equals(request.getReminder())) {
                        estimation.setReminderType(ReminderType.TEN_MINUTE);
                        if (estimation.getFinishTime() != null) {
                            estimation.setNotificationTime(estimation
                                    .getFinishTime()
                                    .minusMinutes(30)
                            );

                        } else {
                            throw new BadCredentialException("Notification finish time must not be null");
                        }
                    }

                    estimation.setCard(card);
                    estimationRepository.save(estimation);
                    card.setEstimation(estimation);
                    log.info("Successfully saved estimation: " + estimation);

                } else {
                    throw new BadRequestException("The finish time can't be before the start time!");
                }

            } else {
                throw new BadRequestException("Finish date can't be before the start date");
            }

        } else {
            throw new BadRequestException("This card already has an estimation");
        }

        return EstimationResponse.builder()
                .estimationId(estimation.getId())
                .startDate(estimation.getStartDate().toString())
                .duetDate(estimation.getFinishDate().toString())
                .finishTime(estimation.getFinishTime().toString())
                .reminderType(estimation.getReminderType())
                .build();
    }

    @Override
    public SimpleResponse updateEstimation(Long estimationId, UpdateEstimationRequest request) {
        Estimation estimation = estimationRepository.findById(estimationId).orElseThrow(() -> {
            log.info("Card with id: " + estimationId + "  not found");
            return new NotFoundException("Card with id: " + estimationId + " id not found");
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

        if (request.getStartTime() != null || !request.getStartTime().toString().isEmpty()) {
            estimation.setFinishTime(request.getFinishTime());
        } else {
            estimation.setFinishTime(estimation.getFinishTime());
        }
        if (request.getFinishTime() != null || !request.getFinishTime().toString().isEmpty()) {
            estimation.setFinishTime(request.getFinishTime());
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
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }

        if (request.getReminder().equals("10")) {
            estimation.setReminderType(ReminderType.TEN_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(10)
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }

        if (request.getReminder().equals("15")) {
            estimation.setReminderType(ReminderType.FIFTEEN_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(15)
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }

        if (request.getReminder().equals("30")) {
            estimation.setReminderType(ReminderType.THIRD_MINUTE);
            if (estimation.getFinishTime() != null) {
                estimation.setNotificationTime(estimation
                        .getFinishTime()
                        .minusMinutes(30)
                );
            } else throw new BadCredentialException("Notification finish time must be not null");
        }
        estimationRepository.save(estimation);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully estimation updated!")
                .build();

    }
}

package peaksoft.house.tasktrackerb9.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import peaksoft.house.tasktrackerb9.enums.ReminderType;

import java.time.ZonedDateTime;

public record EstimationRequest(

        Long cardId,

        String reminder,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:m")
        ZonedDateTime startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
        ZonedDateTime dateOfFinish
) {

}
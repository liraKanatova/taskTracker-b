package peaksoft.house.tasktrackerb9.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import peaksoft.house.tasktrackerb9.enums.ReminderType;

import java.time.ZonedDateTime;

public record
EstimationRequest(

        Long cardId,

        String reminder,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime dateOfFinish
) {

}
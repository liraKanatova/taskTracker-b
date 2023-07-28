package peaksoft.house.tasktrackerb9.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record EstimationRequest(

        Long cardId,

        String reminder,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        ZonedDateTime startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        ZonedDateTime dateOfFinish,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        ZonedDateTime time) {

}
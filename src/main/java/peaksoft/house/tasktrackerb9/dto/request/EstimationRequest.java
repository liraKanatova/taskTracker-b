package peaksoft.house.tasktrackerb9.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
@Getter
@Setter
@NoArgsConstructor

public class EstimationRequest {

    private Long cardId;

    private String reminder;

    //        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime startDate;

    //        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime dateOfFinish;

    //        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime startTime;

    //        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime finishTime;
@Builder
    public EstimationRequest(Long cardId, String reminder, ZonedDateTime startDate, ZonedDateTime dateOfFinish, ZonedDateTime startTime, ZonedDateTime finishTime) {
        this.cardId = cardId;
        this.reminder = reminder;
        this.startDate = startDate;
        this.dateOfFinish = dateOfFinish;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}
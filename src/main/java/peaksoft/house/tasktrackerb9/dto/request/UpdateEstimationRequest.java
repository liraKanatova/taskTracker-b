package peaksoft.house.tasktrackerb9.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.tasktrackerb9.enums.ReminderType;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
public class UpdateEstimationRequest {
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

    public UpdateEstimationRequest(String reminder, ZonedDateTime startDate, ZonedDateTime dateOfFinish, ZonedDateTime startTime, ZonedDateTime finishTime) {
        this.reminder = reminder;
        this.startDate = startDate;
        this.dateOfFinish = dateOfFinish;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}

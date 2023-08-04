package peaksoft.house.tasktrackerb9.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.tasktrackerb9.enums.ReminderType;

@Getter
@Setter
@NoArgsConstructor
public class EstimationResponse {

    private Long estimationId;
    @Enumerated(EnumType.STRING)
    private ReminderType reminderType;
    private String startDate;
    private String duetDate;
    private String finishTime;


    public EstimationResponse(Long estimationId, ReminderType reminderType, String startDate, String duetDate, String finishTime) {
        this.estimationId = estimationId;
        this.reminderType = reminderType;
        this.startDate = startDate;
        this.duetDate = duetDate;
        this.finishTime = finishTime;
    }
}
package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LabelResponse {

    Long labelId;
    String description;
    String color;

    public LabelResponse(Long labelId, String description, String color) {
        this.labelId = labelId;
        this.description = description;
        this.color = color;
    }
}
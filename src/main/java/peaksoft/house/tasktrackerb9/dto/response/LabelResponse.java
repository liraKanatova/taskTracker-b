package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
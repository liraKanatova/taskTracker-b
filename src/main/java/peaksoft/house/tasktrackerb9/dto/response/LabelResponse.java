package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelResponse {

    Long labelId;
    String description;
    String color;

}
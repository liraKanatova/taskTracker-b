package peaksoft.house.tasktrackerb9.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public record BoardRequest (
        String title,
        String backGround

){
}

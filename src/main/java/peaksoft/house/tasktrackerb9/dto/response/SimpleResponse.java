package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleResponse {
    private HttpStatus status;
    private String message;


}

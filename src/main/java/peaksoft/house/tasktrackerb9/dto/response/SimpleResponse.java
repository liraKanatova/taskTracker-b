package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class SimpleResponse {

    private String message;

    private HttpStatus status;


}

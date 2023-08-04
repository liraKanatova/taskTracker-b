package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Builder
@Getter
@Setter
public class SimpleResponse {

    private String message;

    private HttpStatus status;
}

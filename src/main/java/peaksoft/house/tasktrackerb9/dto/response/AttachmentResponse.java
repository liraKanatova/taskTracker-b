package peaksoft.house.tasktrackerb9.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse {

    private Long attachmentId;
    private String documentLink;
    private ZonedDateTime createdAt;
}

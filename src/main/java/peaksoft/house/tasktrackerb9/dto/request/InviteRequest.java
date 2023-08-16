package peaksoft.house.tasktrackerb9.dto.request;

import lombok.*;
import peaksoft.house.tasktrackerb9.enums.Role;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InviteRequest {
    private String email;
    private Role role;
    private String link;
    private Long boardId;

}

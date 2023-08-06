package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;
import peaksoft.house.tasktrackerb9.enums.Role;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Long userId;
    private String firstName;
    private String LastName;
    private String email;
    private String image;
    private Role role;


}

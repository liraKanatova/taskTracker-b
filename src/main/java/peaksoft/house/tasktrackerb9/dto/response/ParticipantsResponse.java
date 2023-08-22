package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.house.tasktrackerb9.enums.Role;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class ParticipantsResponse {

    private Long id;

    private String fullName;

    private String email;

    private Role role;


    public ParticipantsResponse(Long id, String fullName, String email, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
}

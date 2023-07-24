package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private List<WorkSpaceResponse> workSpaceResponse;


    public ProfileResponse(Long userId, String firstName, String lastName, String email, String avatar, List<WorkSpaceResponse> workSpaceResponse) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatar = avatar;
        this.workSpaceResponse = workSpaceResponse;
    }
}

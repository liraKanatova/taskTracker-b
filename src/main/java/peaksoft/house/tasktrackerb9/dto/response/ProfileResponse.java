package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProfileResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private List<WorkSpaceResponse> workSpaceResponse;

    public ProfileResponse(Long id, String firstName, String lastName, String email, String image, List<WorkSpaceResponse> workSpaceResponse) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.workSpaceResponse = workSpaceResponse;
    }
}

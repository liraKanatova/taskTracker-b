package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProfileResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private List<WorkSpaceResponse> workSpaceResponse;


}

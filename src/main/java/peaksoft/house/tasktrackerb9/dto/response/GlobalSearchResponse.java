package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSearchResponse {

    List<UserResponse> userResponses;
    List<BoardResponse>boardResponses;
    List<WorkSpaceResponse> workSpaceResponses;

}

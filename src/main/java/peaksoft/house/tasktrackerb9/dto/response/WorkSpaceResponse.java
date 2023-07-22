package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WorkSpaceResponse {

    private Long workSpaceId;

    private String name;

    public WorkSpaceResponse(Long workSpaceId, String name) {
        this.workSpaceId = workSpaceId;
        this.name = name;
    }
}

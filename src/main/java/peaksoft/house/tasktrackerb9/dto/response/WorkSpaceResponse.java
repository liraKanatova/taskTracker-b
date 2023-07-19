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

    private Long id;

    private String name;

    public WorkSpaceResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

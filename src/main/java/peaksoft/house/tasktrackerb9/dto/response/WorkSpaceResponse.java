package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkSpaceResponse {
    private Long workSpaceId;
    private String workSpaceName;
    private Long adminId;
    private String adminFullName;
    private String adminImage;
    private Boolean isFavorite;


}
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
    private Long id;
    private String name;
    private Long adminId;
    private String fullName;
    private String image;


}

package peaksoft.house.tasktrackerb9.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkSpaceRequest {

    String name;
    List<String> emails;
}

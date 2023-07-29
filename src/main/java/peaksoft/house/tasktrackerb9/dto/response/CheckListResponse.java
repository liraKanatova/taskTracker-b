package peaksoft.house.tasktrackerb9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckListResponse {

    private Long checkListId;
    private String description;
    private int percent;
    private String counter;
    private List<ItemResponse> itemResponseList;

}
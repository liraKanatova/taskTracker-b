package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckListResponse {

    private Long checkListId;
    private String title;
    private int percent;
    private String counter;
    private List<ItemResponse> itemResponseList;

}
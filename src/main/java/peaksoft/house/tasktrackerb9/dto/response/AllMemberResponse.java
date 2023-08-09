package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllMemberResponse {

    private List<MemberResponse> boardMembers;
    private List<MemberResponse> workSpaceMembers;
}

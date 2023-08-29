package peaksoft.house.tasktrackerb9.dto.request;

import lombok.Builder;
import peaksoft.house.tasktrackerb9.enums.Role;

@Builder
public record ChangeRoleRequest(
      Long memberId,
      Long boardId,
      Role role
) {
}

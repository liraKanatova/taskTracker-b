package peaksoft.house.tasktrackerb9.dto.request;

import peaksoft.house.tasktrackerb9.enums.Role;

public record ParticipantsChangeRequest(Long memberId, Long workSpacesId,Role role) {
}

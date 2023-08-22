package peaksoft.house.tasktrackerb9.dto.request;

import lombok.Builder;
import peaksoft.house.tasktrackerb9.enums.Role;

@Builder
public record ParticipantsRequest(String email, String link, Role role, Long workSpacesId) {
}

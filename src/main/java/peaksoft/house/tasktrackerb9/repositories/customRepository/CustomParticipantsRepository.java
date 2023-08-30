package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.enums.Role;

import java.util.List;

public interface CustomParticipantsRepository {

    List<ParticipantsResponse> getParticipantsByRole(Long workSpaceId, Role role);
}

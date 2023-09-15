package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.ParticipantsGetAllResponse;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.enums.Role;

import java.util.List;

public interface CustomParticipantsRepository {

    ParticipantsGetAllResponse getParticipantsByRole(Long workSpaceId, Role role);
}

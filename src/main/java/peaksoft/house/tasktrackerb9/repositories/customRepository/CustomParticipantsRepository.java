package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;

import java.util.List;

public interface CustomParticipantsRepository {

    List<ParticipantsResponse> getAllParticipants(Long workSpaceId);

    List<ParticipantsResponse>getAllAdminParticipants(Long workSpaceId);

    List<ParticipantsResponse>getAllMemberParticipants(Long workSpaceId);
}

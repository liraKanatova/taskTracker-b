package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;

import java.util.List;

public interface CustomParticipantsRepository {

    List<ParticipantsResponse> getAllParticipants(Long workSpacesId);

    List<ParticipantsResponse>getAllAdminParticipants(Long workSpacesId);

    List<ParticipantsResponse>getAllMemberParticipants(Long workSpacesId);
}

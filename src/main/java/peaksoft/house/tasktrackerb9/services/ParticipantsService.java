package peaksoft.house.tasktrackerb9.services;

import jakarta.mail.MessagingException;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsChangeRequest;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsRequest;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface ParticipantsService {

    SimpleResponse inviteToWorkSpaces(ParticipantsRequest request) throws MessagingException;

    SimpleResponse removeToWorkSpaces(Long workSpaceId,Long userId);

    SimpleResponse changeUpdateRole(ParticipantsChangeRequest request);

    List<ParticipantsResponse>getAllParticipants(Long workSpacesId);

    List<ParticipantsResponse>getAllAdminParticipants(Long workSpacesId);

    List<ParticipantsResponse>getAllMemberParticipants(Long workSpacesId);




}

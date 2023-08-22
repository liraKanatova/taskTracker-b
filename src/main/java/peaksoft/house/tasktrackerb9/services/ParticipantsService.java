package peaksoft.house.tasktrackerb9.services;

import jakarta.mail.MessagingException;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsRequest;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;

import java.util.List;

public interface ParticipantsService {


    SimpleResponse inviteToWorkSpaces(ParticipantsRequest request) throws MessagingException;

    SimpleResponse removeToWorkSpaces(Long workSpacesId,Long userId);

    SimpleResponse changeUpdateRole(Long workSpacesId, Long userId, Role role);

    List<ParticipantsResponse>getAllParticipants(Long workSpacesId);

    List<ParticipantsResponse>getAllAdminParticipants(Long workSpacesId);

    List<ParticipantsResponse>getAllMemberParticipants(Long workSpacesId);




}

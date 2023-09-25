package peaksoft.house.tasktrackerb9.services;

import jakarta.mail.MessagingException;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsChangeRequest;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsRequest;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsGetAllResponse;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;

import java.util.List;

public interface ParticipantsService {

    SimpleResponse inviteToWorkSpaces(ParticipantsRequest request) throws MessagingException;

    SimpleResponse removeToWorkSpaces(Long workSpaceId,Long userId);

    SimpleResponse changeUpdateRole(ParticipantsChangeRequest request);

    ParticipantsGetAllResponse getParticipantsByRole(Long workSpaceId, Role role);
}

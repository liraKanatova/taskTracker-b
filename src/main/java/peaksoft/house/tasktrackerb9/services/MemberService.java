package peaksoft.house.tasktrackerb9.services;


import jakarta.mail.MessagingException;
import peaksoft.house.tasktrackerb9.dto.request.ChangeRoleRequest;
import peaksoft.house.tasktrackerb9.dto.request.InviteRequest;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface MemberService {

    List<MemberResponse> searchByEmail(Long workSpaceId, String email);

    AllMemberResponse getAll(Long cardId);

    SimpleResponse inviteMemberToBoard(InviteRequest request) throws MessagingException;

    SimpleResponse changeMemberRole(ChangeRoleRequest request);

    List<MemberResponse> getAllMembersFromBoard(Long boardId);

}
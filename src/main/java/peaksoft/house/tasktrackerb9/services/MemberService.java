package peaksoft.house.tasktrackerb9.services;


import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;

import java.util.List;

public interface MemberService {

    List<MemberResponse> searchByEmail(Long workSpaceId, String email);

    AllMemberResponse getAll(Long cardId);

}

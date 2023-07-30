package peaksoft.house.tasktrackerb9.services;


import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;

import java.util.List;

public interface MemberService {

    List<MemberResponse> searchMemberByWord(Long workSpaceId, String word);

    AllMemberResponse getAll(Long cardId);

}

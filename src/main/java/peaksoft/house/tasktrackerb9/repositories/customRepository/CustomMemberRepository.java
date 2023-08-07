package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;


import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;

import java.util.List;

public interface CustomMemberRepository {

    AllMemberResponse getAll(Long cardId);

    List<MemberResponse> searchByEmail(Long workSpaceId, String email);
}

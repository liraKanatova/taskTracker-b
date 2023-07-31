package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;


import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;

public interface CustomMemberRepository {

    AllMemberResponse getAll(Long cardId);
}

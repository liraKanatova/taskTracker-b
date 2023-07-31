package peaksoft.tasktracker.repository.custom;

import peaksoft.tasktracker.dto.response.AllMemberResponse;

public interface CustomMemberRepository {

    AllMemberResponse getAll(Long cardId);
}

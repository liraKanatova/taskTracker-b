package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.CheckListResponse;

import java.util.List;

public interface CustomCheckListRepository {

    List<CheckListResponse> getAllCheckListByCardId(Long cardId);

}
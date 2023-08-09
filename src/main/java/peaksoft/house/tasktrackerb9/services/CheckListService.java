package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.CheckListRequest;
import peaksoft.house.tasktrackerb9.dto.response.CheckListResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface CheckListService {

    CheckListResponse saveCheckList(Long cardId, CheckListRequest checkListRequest);

    List<CheckListResponse> getAllCheckListByCardId(Long cardId);

    CheckListResponse updateCheckListById(Long checkListId,CheckListRequest checkListRequest);

    SimpleResponse deleteCheckList(Long checkListId);

}
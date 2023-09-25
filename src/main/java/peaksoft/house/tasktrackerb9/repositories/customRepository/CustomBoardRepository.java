package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.FilterBoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.GetAllArchiveResponse;

import java.util.List;

public interface CustomBoardRepository {

    List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId);
    GetAllArchiveResponse getAllArchivedCardsAndColumns(Long boardId);
    FilterBoardResponse filterByConditions(Long boardId, boolean noDates, boolean overdue, boolean dueNextDay, boolean dueNextWeek, boolean dueNextMonth, List<Long>labelIds);

}

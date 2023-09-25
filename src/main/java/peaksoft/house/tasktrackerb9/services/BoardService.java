package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.request.BoardUpdateRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.FilterBoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.GetAllArchiveResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface BoardService {

    List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId);

    BoardResponse saveBoard(BoardRequest boardRequest);

    SimpleResponse updateBoard(BoardUpdateRequest boardUpdateRequest);

    SimpleResponse deleteBoard(Long boardId);

    BoardResponse getBoardById(Long boardId);

    GetAllArchiveResponse getAllArchivedCardsAndColumns(Long boardId);

    FilterBoardResponse filterByConditions(Long boardId, boolean noDates, boolean overdue, boolean dueNextDay, boolean dueNextWeek, boolean dueNextMonth,List<Long>labelIds);

}
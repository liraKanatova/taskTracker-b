package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.request.BoardUpdateRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;

import java.util.List;

public interface BoardService {

    List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId);

    BoardResponse saveBoard(BoardRequest boardRequest);

    SimpleResponse updateBoard(BoardUpdateRequest boardUpdateRequest);

    SimpleResponse deleteBoard(Long boardId);

    BoardResponse getBoardById(Long boardId);

}
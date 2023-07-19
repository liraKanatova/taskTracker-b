package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;

import java.util.List;

public interface BoardService {
    List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId);

    SimpleResponse saveBoard(BoardRequest boardRequest, Long workSpaceId);

    SimpleResponse updateBoard(BoardRequest boardRequest, Long boardId);

    SimpleResponse deleteBoard(Long boardId);

    BoardResponse getBoardById(Long boardId);

}
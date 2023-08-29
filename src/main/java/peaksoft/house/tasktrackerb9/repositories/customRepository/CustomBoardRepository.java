package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.GetAllArchiveResponse;

import java.util.List;

public interface CustomBoardRepository {

    List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId);

    GetAllArchiveResponse getAllArchivedCardsAndColumns(Long boardId);

}

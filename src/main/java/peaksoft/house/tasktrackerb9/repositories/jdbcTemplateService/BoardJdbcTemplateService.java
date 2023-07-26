package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;

import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;

import java.util.List;

public interface BoardJdbcTemplateService {
    List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId);

}

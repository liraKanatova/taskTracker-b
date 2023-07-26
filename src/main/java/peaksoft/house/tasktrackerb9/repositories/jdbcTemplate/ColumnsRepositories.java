package peaksoft.house.tasktrackerb9.repositories.jdbcTemplate;

import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;

import java.util.List;

public interface ColumnsRepositories {

    List<ColumnResponse> getAllColumns(Long boardId);
}

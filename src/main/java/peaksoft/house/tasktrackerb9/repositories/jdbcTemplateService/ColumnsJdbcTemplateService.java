package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;

import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;

import java.util.List;

public interface ColumnsJdbcTemplateService {

    List<ColumnResponse> getAllColumns(Long boardId);
}

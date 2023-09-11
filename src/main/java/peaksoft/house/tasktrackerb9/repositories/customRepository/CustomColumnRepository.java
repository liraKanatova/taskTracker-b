package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;

import java.util.List;

public interface CustomColumnRepository {

    List<ColumnResponse> getAllColumns(Long boardId);
}

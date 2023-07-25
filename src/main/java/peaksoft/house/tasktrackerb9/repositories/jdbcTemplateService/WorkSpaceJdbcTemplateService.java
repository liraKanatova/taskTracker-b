package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;

import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;

import java.util.List;

public interface WorkSpaceJdbcTemplateService {
    List<WorkSpaceResponse> getAllWorkSpaces();

    WorkSpaceResponse getWorkSpaceById(Long workSpaceId);
}

package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;

import java.util.List;

public interface CustomWorkSpaceRepository {
    List<WorkSpaceResponse> getAllWorkSpaces();

    WorkSpaceResponse getWorkSpaceById(Long workSpaceId);
}

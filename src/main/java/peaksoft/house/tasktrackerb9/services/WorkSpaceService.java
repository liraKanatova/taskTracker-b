package peaksoft.house.tasktrackerb9.services;


import jakarta.mail.MessagingException;
import peaksoft.house.tasktrackerb9.dto.request.WorkSpaceRequest;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceFavoriteResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;

import java.util.List;

public interface WorkSpaceService {

    List<WorkSpaceResponse> getAllWorkSpaces();

    WorkSpaceFavoriteResponse saveWorkSpace(WorkSpaceRequest workSpaceRequest) throws MessagingException;

    WorkSpaceResponse getWorkSpaceById(Long id);

    SimpleResponse updateWorkSpaceById(Long id, WorkSpaceRequest workSpaceRequest);

    SimpleResponse deleteWorkSpaceById(Long id);

}
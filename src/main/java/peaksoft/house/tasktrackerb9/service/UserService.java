package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.request.UserRequestImage;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.entity.WorkSpace;

import java.util.List;

public interface UserService {

    SimpleResponse userUpdating(Long id, UserRequest userRequest);

    SimpleResponse updatingImage(Long id, UserRequestImage image);

    UserResponse getByIdUser(Long id);

    List<WorkSpace> userGetAllWorkSpace();

    SimpleResponse deleteProfileUser(Long id);

}

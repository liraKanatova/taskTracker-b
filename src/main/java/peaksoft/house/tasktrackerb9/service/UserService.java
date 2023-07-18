package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.request.UserRequestImage;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.entity.WorkSpace;

import java.util.List;

public interface UserService {

    SimpleResponse updateUserById(Long id, UserRequest userRequest);

    SimpleResponse updateImageUserId(Long id, UserRequestImage image);

    UserResponse getUserById(Long id);

    List<WorkSpace> userGetAllWorkSpace();

    SimpleResponse deleteProfileUser(Long id);

}
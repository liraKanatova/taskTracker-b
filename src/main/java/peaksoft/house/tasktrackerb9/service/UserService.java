package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;

public interface UserService {

    SimpleResponse updateUserBy(UserRequest userRequest);

    SimpleResponse updateImageUserId(Long id, String image);

    UserResponse getUserById(Long id);

    ProfileResponse getProfileById(Long id);

    SimpleResponse removeProfileUser(Long id);

}
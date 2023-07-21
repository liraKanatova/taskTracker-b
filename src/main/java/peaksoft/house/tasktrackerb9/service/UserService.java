package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

public interface UserService {

    SimpleResponse updateUserBy(UserRequest userRequest);

    SimpleResponse updateImageUserId(Long userId, String image);

    ProfileResponse getProfileById(Long userId);

}
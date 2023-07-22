package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> updateUser(UserRequest userRequest);

     List<UserResponse>updateImageUserId(Long userId, String image);

    ProfileResponse getProfileById(Long userId);

}
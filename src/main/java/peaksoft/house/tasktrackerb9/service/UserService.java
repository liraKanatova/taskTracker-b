package peaksoft.house.tasktrackerb9.service;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;


public interface UserService {

    UserResponse  updateUser(UserRequest userRequest);

     UserResponse updateImageUserId( String image);

    ProfileResponse getProfileById(Long userId);



}
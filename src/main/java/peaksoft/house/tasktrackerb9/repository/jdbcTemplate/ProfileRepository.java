package peaksoft.house.tasktrackerb9.repository.jdbcTemplate;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;


public interface ProfileRepository {

    UserResponse updateUser(UserRequest userRequest);

    UserResponse updateImageUserId (String image);

    ProfileResponse getProfileById(Long userId);

    UserResponse getMyProfile();



}

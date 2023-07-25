package peaksoft.house.tasktrackerb9.repositories.jdbcTemplate;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;


public interface ProfileRepository {

    UserResponse updateUser(UserRequest userRequest);

    ProfileResponse getProfileById(Long userId);
}

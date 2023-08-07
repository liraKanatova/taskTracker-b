package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.GlobalSearchResponse;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;


public interface CustomProfileRepository {

    UserResponse updateUser(UserRequest userRequest);

    ProfileResponse getProfileById(Long userId);

    GlobalSearchResponse search(String search);
}

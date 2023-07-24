package peaksoft.house.tasktrackerb9.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.repository.jdbcTemplate.ProfileImpl;
import peaksoft.house.tasktrackerb9.service.UserService;



@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final ProfileImpl queryJdbc;


    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        return queryJdbc.updateUser(userRequest);
    }

    @Override
    public UserResponse updateImageUserId(String image) {
        return queryJdbc.updateImageUserId(image);
    }

    @Override
    public ProfileResponse getProfileById(Long userId) {
        return queryJdbc.getProfileById(userId);
    }

    @Override
    public UserResponse getMyProfile() {
        return queryJdbc.getMyProfile();
    }
}

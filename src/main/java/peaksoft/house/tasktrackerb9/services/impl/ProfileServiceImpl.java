package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl.CustomProfileRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.ProfileService;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final CustomProfileRepositoryImpl queryJdbc;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        return queryJdbc.updateUser(userRequest);
    }

    @Override
    public UserResponse updateImageUserId(String image) {
        User user = jwtService.getAuthentication();
        user.setImage(image);
        userRepository.save(user);
        log.info("Updated image user");
        return UserResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getImage())
                .build();
    }

    @Override
    public ProfileResponse getProfileById(Long userId) {
        return queryJdbc.getProfileById(userId);
    }

    @Override
    public UserResponse getMyProfile() {
        User user = jwtService.getAuthentication();
        UserResponse userResponse =
                new UserResponse(
                        user.getId()
                        , user.getFirstName()
                        , user.getLastName()
                        , user.getEmail()
                        , user.getImage());
        jwtService.generateToken(user);
        return userResponse;
    }
}

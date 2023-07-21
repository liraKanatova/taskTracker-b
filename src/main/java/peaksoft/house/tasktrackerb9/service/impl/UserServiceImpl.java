package peaksoft.house.tasktrackerb9.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;
import peaksoft.house.tasktrackerb9.entity.User;
import peaksoft.house.tasktrackerb9.entity.WorkSpace;
import peaksoft.house.tasktrackerb9.exception.NotFoundException;
import peaksoft.house.tasktrackerb9.repository.UserRepository;
import peaksoft.house.tasktrackerb9.repository.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final WorkSpaceRepository workSpaceRepository;

    private final JwtService jwtService;


    @Override
    public SimpleResponse updateUserBy(UserRequest userRequest) {

        User user = jwtService.getAuthentication();
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
            user.setPassword(passwordEncoder.encode(userRequest.password()));
            userRepository.save(user);
            log.info("Updated user");
        return SimpleResponse
                .builder()
                .message("Updated")
                .status(HttpStatus.OK)
                .build();

    }

    @Override
    public SimpleResponse updateImageUserId(Long userId, String image) {

        User users = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id: " + userId + " not found");
            return new NotFoundException("User with id: " + userId + " not found");
        });
        users.setImage(image);
        userRepository.save(users);

        return
                SimpleResponse
                        .builder()
                        .message("Update avatar")
                        .status(HttpStatus.OK)
                        .build();
    }

    @Override
    public ProfileResponse getProfileById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));

        List<WorkSpace> workSpaces = new ArrayList<>();
        for (WorkSpace w : workSpaceRepository.findAll()) {
            for (User u : w.getUsers()) {
                if (u.equals(user)) {
                    workSpaces.add(w);
                }
            }
        }
        return
                ProfileResponse
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .image(user.getImage())
                        .workSpaceResponse(workSpaces.stream()
                                .map(x -> new WorkSpaceResponse(x.getId(), x.getName()))
                                .toList())
                        .build();
    }
}

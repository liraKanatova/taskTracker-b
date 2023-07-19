package peaksoft.house.tasktrackerb9.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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


    public User getAuthentication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User not found!"));
    }

    @Override
    public SimpleResponse updateUserById(UserRequest userRequest) {

        User user = getAuthentication();
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
        if (userRequest.password().equals(userRequest.repeatPassword())) {
            user.setPassword(passwordEncoder.encode(userRequest.password()));
            userRepository.save(user);
        } else {
            throw new NotFoundException("Password do not match");
        }
        return SimpleResponse
                .builder()
                .message("Updated")
                .status(HttpStatus.OK)
                .build();

    }


    @Override
    public SimpleResponse updateImageUserId(Long id, String image) {

        User user = getAuthentication();
        User users = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
        users.setImage(image);
        if (user.getImage().equals(users.getImage())) {
            userRepository.save(users);
        }
        return
                SimpleResponse
                        .builder()
                        .message("Save entity")
                        .status(HttpStatus.OK)
                        .build();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
        return
                UserResponse
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .image(user.getImage())
                        .build();
    }

    @Override
    public ProfileResponse getProfileById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));

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
                        .workSpaceResponse(workSpaces.stream().map(x -> new WorkSpaceResponse(x.getId(), x.getName())).toList())
                        .build();
    }

    @Override
    public SimpleResponse deleteProfileUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
        user.setImage(null);
        userRepository.save(user);

        return
                SimpleResponse
                        .builder()
                        .message("Successfully deleted")
                        .status(HttpStatus.OK)
                        .build();
    }
}

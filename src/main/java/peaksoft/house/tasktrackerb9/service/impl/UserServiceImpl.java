package peaksoft.house.tasktrackerb9.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.request.UserRequestImage;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.entity.User;
import peaksoft.house.tasktrackerb9.entity.WorkSpace;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exception.NotFoundException;
import peaksoft.house.tasktrackerb9.repository.UserRepository;
import peaksoft.house.tasktrackerb9.repository.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
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
    public SimpleResponse updateUserById(Long id, UserRequest userRequest) {

        User user = getAuthentication();
        User users = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: "+id+" not found"));
        users.setFirstName(userRequest.firstName());
        users.setLastName(userRequest.lastName());
        users.setEmail(userRequest.email());
        if (userRequest.password().equals(userRequest.repeatPassword())) {
            users.setPassword(passwordEncoder.encode(userRequest.password()));
        }
        if (user.getRole().equals(Role.ADMIN)) {
            userRepository.save(users);
            return SimpleResponse.builder().message("Ok").status(HttpStatus.OK).build();
        } else if (users.equals(user)) {
            userRepository.save(users);
        }
        return SimpleResponse.builder().message("Updated").status(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse updateImageUserId(Long id, UserRequestImage image) {

        User user = getAuthentication();
        User users = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: "+id+" not found"));
        users.setImage(image.image());
        if (user.getImage().equals(users.getImage())) {
            userRepository.save(users);
        }
        return SimpleResponse.builder().message("Save entity").status(HttpStatus.OK).build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: "+id+" not found"));
        return UserResponse.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).password(user.getPassword()).image(user.getImage()).build();
    }

    @Override
    public List<WorkSpace> userGetAllWorkSpace() {

        User user = getAuthentication();
        List<WorkSpace> workSpaces = new ArrayList<>();
        for (WorkSpace w : workSpaceRepository.findAll()) {
            for (User u : w.getUsers()) {
                if (u.equals(user)) {
                    workSpaces.add(w);
                }
            }
        }
        return workSpaces;
    }

    @Override
    public SimpleResponse deleteProfileUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: "+id+" not found"));
        userRepository.delete(user);
        return SimpleResponse.builder().message("Successfully deleted").status(HttpStatus.OK).build();
    }
}

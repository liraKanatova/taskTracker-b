package peaksoft.house.tasktrackerb9.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
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
import peaksoft.house.tasktrackerb9.repository.jdbcTemplate.QueryJdbc;
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

    private final JdbcTemplate jdbcTemplate;

    private final QueryJdbc queryJdbc;

    private final JwtService jwtService;


    @Override
    public List<UserResponse> updateUser(UserRequest userRequest) {

        User user =jwtService.getAuthentication();
        List<UserResponse> list = jdbcTemplate.query(queryJdbc.getUpdateUser(), (rs, rowNum)
                -> new UserResponse(rs.getLong("userId"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("avatar")
        ),userRequest.firstName()
                ,userRequest.lastName()
                ,userRequest.email()
                ,userRequest.password()
                ,user.getId());

        return list.stream()
                .map(x->new UserResponse
                        (x.getUserId()
                                ,x.getFirstName()
                                ,x.getLastName()
                                ,x.getEmail()
                                ,x.getPassword()
                                ,x.getAvatar()))
                .toList();

    }

    @Override
    public List<UserResponse> updateImageUserId(Long userId, String image) {

        List<UserResponse>list=jdbcTemplate.query(queryJdbc.getGetById(),new Object[]{userId},(rs, rowNum) ->
                new UserResponse(rs.getLong("userId")
                        ,rs.getString("firstName")
                        ,rs.getString("lastName")
                        ,rs.getString("email")
                        ,rs.getString("password")
                        ,rs.getString("avatar")
                        ));


        users.setImage(image);
        userRepository.save(users);
        log.info("Updated user");
        return
                SimpleResponse
                        .builder()
                        .message("Update avatar")
                        .status(HttpStatus.OK)
                        .build();
    }

    @Override
    public ProfileResponse getProfileById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));

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
                        .userId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .avatar(user.getImage())
                        .workSpaceResponse(workSpaces.stream()
                                .map(x -> new WorkSpaceResponse(x.getId(), x.getName()))
                                .toList())
                        .build();
    }
}

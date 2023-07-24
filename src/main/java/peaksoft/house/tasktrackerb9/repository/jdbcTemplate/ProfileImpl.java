package peaksoft.house.tasktrackerb9.repository.jdbcTemplate;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;
import peaksoft.house.tasktrackerb9.entity.User;
import peaksoft.house.tasktrackerb9.exception.NotFoundException;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileImpl implements ProfileRepository {

    private final JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse updateUser(UserRequest userRequest) {

        User user = jwtService.getAuthentication();
        String query = "UPDATE users u SET u.first_name=?,u.last_name=?,u.email=?,u.password=? WHERE u.id=:id";

        UserResponse userResponse = jdbcTemplate.queryForObject(query, (rs, rowNum) ->
                        new UserResponse(rs.getLong("userId")
                                , rs.getString("firstName")
                                , rs.getString("lastName")
                                , rs.getString("email")
                                , rs.getString("password")
                                , rs.getString("avatar")),
                userRequest.firstName()
                , userRequest.lastName()
                , userRequest.email()
                , passwordEncoder.encode(userRequest.password())
                , user.getId());

        assert userResponse != null;

        return UserResponse.builder()
                .userId(userResponse.getUserId())
                .firstName(userResponse.getFirstName())
                .lastName(userResponse.getLastName())
                .email(userResponse.getEmail())
                .password(userResponse.getPassword())
                .avatar(userResponse.getAvatar())
                .build();

    }

    @Override
    public UserResponse updateImageUserId(String image) {

        String jdbc = "UPDATE users u SET u.image=?";

        UserResponse userResponses = jdbcTemplate.queryForObject(jdbc, (rs, rowNum) ->
                        new UserResponse(rs.getLong("userId")
                                , rs.getString("firstName")
                                , rs.getString("lastName")
                                , rs.getString("email")
                                , rs.getString("password")
                                , rs.getString("avatar")),
                image);
        assert userResponses != null;

        return UserResponse.builder()
                .userId(userResponses.getUserId())
                .firstName(userResponses.getFirstName())
                .lastName(userResponses.getLastName())
                .email(userResponses.getEmail())
                .password(userResponses.getPassword())
                .avatar(userResponses.getAvatar())
                .build();
    }

    @Override
    public ProfileResponse getProfileById(Long userId) {

        String query = "SELECT id, first_name, last_name, email, image FROM users WHERE id = ?";

        User user = jdbcTemplate.queryForObject(query, ((rs, rowNum) ->
                new User(rs.getLong("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("image"))), userId);

        if (user == null) {
            throw new NotFoundException("User with id: " + userId + " not found");
        }

        String workspaceSql = "SELECT w.id, w.name FROM workspace w " +
                "INNER JOIN user_workspace uw ON w.id = uw.workspace_id " +
                "WHERE uw.user_id = ?";

        List<WorkSpaceResponse> workSpaceResponses = jdbcTemplate.query(workspaceSql, ((rs, rowNum) ->
                new WorkSpaceResponse(rs.getLong("id"),
                        rs.getString("name"))), user.getId());

        return ProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getImage())
                .workSpaceResponse(workSpaceResponses)
                .build();
    }

    @Override
    public UserResponse getMyProfile() {

        User user = jwtService.getAuthentication();

        String query="select u.id, u.first_name, u.last_name,u.email, u.password, u.image from users u where u=:?";

        return jdbcTemplate.queryForObject(query, ((rs, rowNum) ->
                new UserResponse(rs.getLong("userId")
                        , rs.getString("firstName")
                        , rs.getString("lastName")
                        , rs.getString("email")
                        , rs.getString("password")
                        , rs.getString("avatar"))), user);
    }
}



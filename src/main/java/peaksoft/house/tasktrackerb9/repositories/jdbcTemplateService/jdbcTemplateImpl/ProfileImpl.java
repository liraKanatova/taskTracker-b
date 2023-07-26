package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.ProfileRepository;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileImpl implements ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        User user=jwtService.getAuthentication();
        String query = "UPDATE users AS u SET first_name=?,last_name=?,email=?,password=? WHERE u.id=?";

        jdbcTemplate.update(query,
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.email(),
                passwordEncoder.encode(userRequest.password()),
                user.getId());

        return UserResponse.builder()
                .userId(user.getId())
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .avatar(user.getImage())
                .build();
    }

    @Override
    public ProfileResponse getProfileById(Long userId) {
        String query = """
                SELECT u.id, email, first_name, image, last_name
                FROM users u
                WHERE u.id = ?
                """;
        User user = jdbcTemplate.queryForObject(query, (rs, rowNum) ->
                new User(rs.getLong("id")
                        , rs.getString("first_name")
                        , rs.getString("last_name")
                        , rs.getString("email")
                        , rs.getString("image")), userId);
        String query1 = """
                SELECT ws.*
                FROM users u
                         JOIN users_spaces uws on u.id = uws.users_id
                         JOIN work_spaces ws on uws.spaces_id = ws.id = ws.id
                WHERE u.id = ?
                """;
        List<WorkSpaceResponse> workSpaceResponses = jdbcTemplate.query(query1, (rs, rowNum) -> new WorkSpaceResponse(
                rs.getLong("id"),
                rs.getString("name")
        ), userId);
        assert user != null;
        return ProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getImage())
                .workSpaceResponse(workSpaceResponses)
                .build();
    }
}



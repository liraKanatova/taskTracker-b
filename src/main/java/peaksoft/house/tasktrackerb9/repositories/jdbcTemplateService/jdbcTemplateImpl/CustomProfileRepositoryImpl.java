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
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CustomProfileRepository;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomProfileRepositoryImpl implements CustomProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        User user = jwtService.getAuthentication();
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
                        new User(rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("image")
                        ),
                userId);
        String query1 = """
                SELECT ws.id,ws.name
                FROM users u
                         JOIN users_work_spaces uws ON u.id = uws.members_id
                         JOIN work_spaces ws ON uws.work_spaces_id = ws.id
                WHERE u.id = ?;
                        """;
        List<WorkSpaceResponse> workSpaceResponses = jdbcTemplate.query(query1, (rs, rowNum) -> new WorkSpaceResponse(
                rs.getLong("id"),
                rs.getString("name")
        ), userId);
        String sql = """
                       SELECT\s
                           u.id AS userId,
                           u.first_name AS firstName,
                           u.last_name AS lastName,
                           u.email AS email,
                           u.image AS avatar,
                           (SELECT COUNT(*)\s
                            FROM users AS u2

                            JOIN users_work_spaces uws ON u2.id = uws.members_id
                            JOIN work_spaces ws ON ws.id = uws.work_spaces_id
                WHERE u2.id = u.id) AS countWorkSpaces
                       FROM users AS u
                       WHERE u.id = ?;
                        
                                         """;
        ProfileResponse profileResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ProfileResponse(rs.getLong("userId")
                , rs.getString("firstName")
                , rs.getString("lastName")
                , rs.getString("email")
                , rs.getString("avatar")
                , rs.getInt("countWorkSpaces")
        ), userId);
        assert profileResponse != null;
        int count = profileResponse.getCountWorkSpaces();
        profileResponse.setCountWorkSpaces(count);
        assert user != null;
        return ProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getImage())
                .countWorkSpaces(count)
                .workSpaceResponse(workSpaceResponses)
                .build();
    }

    @Override
    public ProfileResponse getMyProfile() {

        User user = jwtService.getAuthentication();

        String query1 = """
                SELECT ws.id,ws.name
                FROM users u
                         JOIN users_work_spaces uws ON u.id = uws.members_id
                         JOIN work_spaces ws ON uws.work_spaces_id = ws.id
                WHERE u.id = ?;
                        """;
        List<WorkSpaceResponse> workSpaceResponses = jdbcTemplate.query(query1, (rs, rowNum) -> new WorkSpaceResponse(
                rs.getLong("id"),
                rs.getString("name")
        ), user.getId());
        String sql = """
                       SELECT\s
                           u.id AS userId,
                           u.first_name AS firstName,
                           u.last_name AS lastName,
                           u.email AS email,
                           u.image AS avatar,
                           (SELECT COUNT(*)\s
                            FROM users AS u2

                            JOIN users_work_spaces uws ON u2.id = uws.members_id
                            JOIN work_spaces ws ON ws.id = uws.work_spaces_id
                WHERE u2.id = u.id) AS countWorkSpaces
                       FROM users AS u
                       WHERE u.id = ?;
                        
                                         """;
        ProfileResponse profileResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ProfileResponse(rs.getLong("userId")
                , rs.getString("firstName")
                , rs.getString("lastName")
                , rs.getString("email")
                , rs.getString("avatar")
                , rs.getInt("countWorkSpaces")
        ), user.getId());
        assert profileResponse != null;
        int count = profileResponse.getCountWorkSpaces();
        profileResponse.setCountWorkSpaces(count);

        return ProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getImage())
                .countWorkSpaces(count)
                .workSpaceResponse(workSpaceResponses)
                .build();
    }
}



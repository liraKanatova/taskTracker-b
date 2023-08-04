package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CustomWorkSpaceRepository;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
@Getter
public class CustomWorkSpaceRepositoryImpl implements CustomWorkSpaceRepository {
    private final JwtService jwtService;
    private final JdbcTemplate jdbcTemplate;

    public List<WorkSpaceResponse> getAllWorkSpaces() {
        User user = jwtService.getAuthentication();
        String sql = """
               SELECT w.id                                                AS id,
                      w.name                                              AS workSpaceName,
                      u.id                                                AS userId,
                      concat(u.first_name, '  ', u.last_name)             AS fullName,
                      u.image                                             AS image,
                      CASE WHEN f.id IS NOT NULL THEN TRUE ELSE FALSE END AS isFavorite
               FROM work_spaces
                        AS w
                        JOIN users AS u ON w.admin_id = u.id
                        LEFT JOIN
                    favorites f on u.id = f.member_id
               where u.id = ?
                """;
        return jdbcTemplate.query(sql,
                new Object[]{user.getId()}, (rs, rowNum) -> {
                    WorkSpaceResponse workSpaceResponse = new WorkSpaceResponse();
                    workSpaceResponse.setWorkSpaceId(rs.getLong("id"));
                    workSpaceResponse.setWorkSpaceName(rs.getString("workSpaceName"));
                    workSpaceResponse.setAdminId(rs.getLong("userId"));
                    workSpaceResponse.setAdminFullName(rs.getString("fullName"));
                    workSpaceResponse.setAdminImage(rs.getString("image"));
                    workSpaceResponse.setIsFavorite(rs.getBoolean("isFavorite"));
                    return workSpaceResponse;
                }
        );
    }

    @Override
    public WorkSpaceResponse getWorkSpaceById(Long workSpaceId) {
        String sql = """
            SELECT w.id                                                AS id,
                   w.name                                              AS WorkSpaceName,
                   u.id                                                AS userId,
                   CONCAT(u.first_name, ' ', u.last_name)              AS fullName,
                   u.image                                             AS image,
                   CASE WHEN f.id IS NOT NULL THEN TRUE ELSE FALSE END AS isFavorite
            FROM work_spaces w
                     JOIN users u ON w.admin_id = u.id
                     LEFT JOIN favorites f ON u.id = f.member_id
            WHERE u.id = ?
              AND w.id = ?
                """;
        User user = jwtService.getAuthentication();
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{user.getId(), workSpaceId},
                (rs, rowNum) -> {
                    WorkSpaceResponse response = new WorkSpaceResponse();
                    response.setWorkSpaceId(rs.getLong("id"));
                    response.setWorkSpaceName(rs.getString("workSpaceName"));
                    response.setAdminId(rs.getLong("userId"));
                    response.setAdminFullName(rs.getString("fullName"));
                    response.setAdminImage(rs.getString("image"));
                    response.setIsFavorite(rs.getBoolean("isFavorite"));
                    return response;
                }
        );
    }

}


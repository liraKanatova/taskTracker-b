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
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.WorkSpaceJdbcTemplateService;

import java.util.List;


@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
@Getter
public class WorkSpaceJdbcTemplateServiceImpl implements WorkSpaceJdbcTemplateService {
    private final WorkSpaceRepository workSpaceRepository;
    private final JwtService jwtService;
    private final JdbcTemplate jdbcTemplate;

    public List<WorkSpaceResponse> getAllWorkSpaces() {
        User user = jwtService.getAuthentication();
        List<WorkSpaceResponse> workSpaceResponses = jdbcTemplate.query(getAllWorkSpacesQuery(),
                new Object[]{user.getId()}, (rs, rowNum) -> {
                    WorkSpaceResponse workSpaceResponse = new WorkSpaceResponse();
                    workSpaceResponse.setWorkSpaceId(rs.getLong("id"));
                    workSpaceResponse.setWorkSpaceName(rs.getString("workSpaceName"));
                    workSpaceResponse.setAdminId(rs.getLong("userId"));
                    workSpaceResponse.setAdminFullName(rs.getString("fullName"));
                    workSpaceResponse.setAdminImage(rs.getString("image"));
                    return workSpaceResponse;
                }
        );
        return workSpaceResponses;
    }

    @Override
    public WorkSpaceResponse getWorkSpaceById(Long workSpaceId) {
        User user = jwtService.getAuthentication();
        String getWorkSpaceByIdSql = getWorkSpaceByIdQuery();
        WorkSpaceResponse workSpaceResponse = jdbcTemplate.queryForObject(
                getWorkSpaceByIdSql,
                new Object[]{user.getId(), workSpaceId},
                (rs, rowNum) -> {
                    WorkSpaceResponse response = new WorkSpaceResponse();
                    response.setWorkSpaceId(rs.getLong("id"));
                    response.setWorkSpaceName(rs.getString("workSpaceName"));
                    response.setAdminId(rs.getLong("userId"));
                    response.setAdminFullName(rs.getString("fullName"));
                    response.setAdminImage(rs.getString("image"));
                    return response;
                }
        );
        return workSpaceResponse;
    }

    private String getAllWorkSpacesQuery() {
        return "SELECT w.id AS id, w.name AS workSpaceName," +
                "  u.id AS userId," +
                "  CONCAT(u.first_name, ' ', u.last_name) AS fullName," +
                "  u.image as image" +
                "  FROM work_spaces AS w" +
                " JOIN users AS u ON w.admin_id = u.id" +
                "  WHERE u.id = ?";
    }

    private String getWorkSpaceByIdQuery() {
        return "SELECT w.id AS id, w.name AS workSpaceName," +
                "  u.id AS userId," +
                "  CONCAT(u.first_name, ' ', u.last_name) AS fullName," +
                "  u.image as image" +
                "  FROM work_spaces AS w" +
                " JOIN users AS u ON w.admin_id = u.id" +
                "  WHERE u.id = ? AND w.id = ?";
    }

}


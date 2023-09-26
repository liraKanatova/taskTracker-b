package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsGetAllResponse;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomParticipantsRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomParticipantsRepositoryImpl implements CustomParticipantsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final JwtService jwtService;

    @Override
    public ParticipantsGetAllResponse getParticipantsByRole(Long workSpaceId, Role role) {
        if (workSpaceId == null) {
            throw new NotFoundException("WorkSpace ID cannot be null");
        }

        User user = jwtService.getAuthentication();
        String sql;
        Object[] params;

        sql = """
        SELECT u.id AS id, 
        CONCAT(u.first_name, ' ', u.last_name) AS fullname,
        u.email AS email,
        uwsr.role AS role,
        ? AS isAdmin
        FROM user_work_space_roles uwsr
        JOIN work_spaces ws ON ws.id = uwsr.work_space_id
        JOIN users u ON uwsr.member_id = u.id
        WHERE ws.id = ? AND (uwsr.role = ? OR uwsr.role = ?) AND u.id != ws.admin_id; 
        """;
        params = new Object[]{user.getId().equals(getWorkspaceCreatorId(workSpaceId)), workSpaceId, Role.ADMIN.toString(), Role.MEMBER.toString()};

        List<ParticipantsResponse> participantsResponses = jdbcTemplate.query(sql, params, (rs, rowNum) ->
                new ParticipantsResponse(
                        rs.getLong("id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role")),
                        rs.getBoolean("isAdmin")
                ));

        return ParticipantsGetAllResponse.builder()
                .participantsResponseList(participantsResponses)
                .isMy(user.getId().equals(getWorkspaceCreatorId(workSpaceId)))
                .build();
    }
    private Long getWorkspaceCreatorId(Long workSpaceId) {
        String sql = "SELECT admin_id FROM work_spaces WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, workSpaceId);
    }




}

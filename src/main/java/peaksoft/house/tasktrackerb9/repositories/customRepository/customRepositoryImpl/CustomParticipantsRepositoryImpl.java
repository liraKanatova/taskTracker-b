package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomParticipantsRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomParticipantsRepositoryImpl implements CustomParticipantsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ParticipantsResponse> getParticipantsByRole(Long workSpaceId, Role role) {
        if (workSpaceId == null) {
            throw new NotFoundException("WorkSpace ID cannot be null");
        }
        String sql;
        Object[] params;

        if (role == Role.ALL) {
            sql = """
                    SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) AS fullname, u.email, uwsr.role
                    FROM user_work_space_roles uwsr
                    JOIN work_spaces ws ON ws.id = uwsr.work_space_id
                    JOIN users u ON uwsr.member_id = u.id
                    WHERE ws.id = ? AND (uwsr.role = ? OR uwsr.role = ?) and u.id != ws.admin_id ; 
                    """;
            params = new Object[]{workSpaceId, Role.ADMIN.toString(), Role.MEMBER.toString()};
        } else {
            sql = """
                    SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) AS fullname, u.email, uwsr.role
                    FROM user_work_space_roles uwsr
                    JOIN work_spaces ws ON ws.id = uwsr.work_space_id
                    JOIN users u ON uwsr.member_id = u.id
                    WHERE ws.id = ? AND uwsr.role = ? AND u.id != ws.admin_id;  
                    """;
            params = new Object[]{workSpaceId, role.toString()};
        }

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            boolean isAdmin = rs.getString("role").equals(Role.ADMIN.toString());
            return new ParticipantsResponse(
                    rs.getLong("id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role")),
                    isAdmin
            );
        }, params);
    }


}

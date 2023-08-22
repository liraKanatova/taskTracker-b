package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomParticipantsRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomParticipantsRepositoryImpl implements CustomParticipantsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ParticipantsResponse> getAllParticipants(Long workSpacesId) {

        String sql = """
            SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) AS fullname, u.email, u.role
            FROM users u
            JOIN users_work_spaces usw ON u.id = usw.members_id
            JOIN work_spaces w ON usw.work_spaces_id = w.id
            WHERE w.id = ?;
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new ParticipantsResponse(
                        rs.getLong("id"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                ),
                workSpacesId
        );
    }

    @Override
    public List<ParticipantsResponse> getAllAdminParticipants(Long workSpacesId) {

        String sql= """
                SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) AS fullname, u.email, u.role
                FROM users u
                JOIN users_work_spaces usw ON u.id = usw.members_id
                JOIN work_spaces w ON usw.work_spaces_id = w.id
                WHERE w.id = ? and u.role='ADMIN'; 
                               
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new ParticipantsResponse(
                                rs.getLong("id"),
                                rs.getString("fullname"),
                                rs.getString("email"),
                                Role.valueOf(rs.getString("role"))
                        ),
                workSpacesId
        );
    }

    @Override
    public List<ParticipantsResponse> getAllMemberParticipants(Long workSpacesId) {
        String sql= """
                SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) AS fullname, u.email, u.role
                FROM users u
                JOIN users_work_spaces usw ON u.id = usw.members_id
                JOIN work_spaces w ON usw.work_spaces_id = w.id
                WHERE w.id = ? and u.role='MEMBER'; 
                               
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new ParticipantsResponse(
                                rs.getLong("id"),
                                rs.getString("fullname"),
                                rs.getString("email"),
                                Role.valueOf(rs.getString("role"))
                        ),
                workSpacesId
        );
    }
}

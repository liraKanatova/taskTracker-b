package peaksoft.house.tasktrackerb9.repositories.jdbcTemplate;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.models.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class BoardJdbcTemplateIml {

    private final JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;

    public List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId) {

        User user = jwtService.getAuthentication();
        String sql = "SELECT b.id, b.title, b.back_ground " +
                "FROM boards b " +
                "         JOIN favorites f on b.id = f.board_id " +
                "         JOIN users u on f.user_id = u.id " +
                "         JOIN work_spaces ws on b.work_space_id = ws.id " +
                "WHERE u.id = ? and ws.id = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new BoardResponse(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("back_ground")),
                user.getId(), workSpaceId
        );
    }
}

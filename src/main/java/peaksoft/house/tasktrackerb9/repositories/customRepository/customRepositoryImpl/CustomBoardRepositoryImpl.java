package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomBoardRepository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Transactional
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId) {

        User user = jwtService.getAuthentication();
        String sql = "" +
                " SELECT b.id, b.title, b.back_ground, "+
                " CASE WHEN f.board_id IS NOT NULL THEN TRUE ELSE FALSE END AS isFavorite "+
                " FROM boards b "+
                " JOIN work_spaces ws ON b.work_space_id = ws.id "+
                " LEFT JOIN favorites f ON b.id = f.board_id AND f.user_id = ? "+
                " WHERE ws.id = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new BoardResponse(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("back_ground"),
                        rs.getBoolean("isFavorite")),
                        user.getId(),workSpaceId);
    }



}

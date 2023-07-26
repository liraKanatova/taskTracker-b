package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CommentJdbcTemplateService;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentJdbcTemplateServiceImpl implements CommentJdbcTemplateService {

    private final JdbcTemplate jdbcTemplate;

    private String getAllQuery() {
        String sql = "SELECT c.id AS id ,c.comment AS comment,c.created_date AS date FROM comments AS c join users u on u.id = c.user_id\n" +
                "    LEFT  JOIN cards_users cu on u.id = cu.users_id where user_id=?";
        return sql;
    }

    @Override
    public List<CommentResponse> getAllComments(Long userId) {
        try {
            List<CommentResponse> commentResponses = jdbcTemplate.query(getAllQuery(), new Object[]{userId}, ((rs, rowNum) -> {
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setId(rs.getLong("id"));
                commentResponse.setComment(rs.getString("comment"));
                commentResponse.setCreatedDate(rs.getDate("date"));
                return commentResponse;
            }));
            return commentResponses;
        } catch (NotFoundException e) {
            log.error(String.format("User with id: %s not found !", userId));
            throw new NotFoundException(String.format("User with id: %s not found !", userId));
        }
    }

    private String getCommentByIdQuery() {
        String sql = "SELECT c.id AS id, c.comment AS comment,c.created_date AS date FROM comments AS c \n" +
                "    JOIN users u ON u.id = c.user_id WHERE u.id=? AND c.id=?";
        return sql;
    }

    @Override
    public CommentResponse getCommentById(Long userId, Long commentId) {
        Optional<CommentResponse> commentResponse = Optional.ofNullable(jdbcTemplate.queryForObject
                (getCommentByIdQuery(), new Object[]{userId, commentId}, (rs, rowNum) -> {
                    CommentResponse commentResponse1 = new CommentResponse();
                    commentResponse1.setId(rs.getLong("id"));
                    commentResponse1.setComment(rs.getString("comment"));
                    commentResponse1.setCreatedDate(rs.getDate("date"));
                    return commentResponse1;
                }));
        return commentResponse.orElseThrow(() -> new NotFoundException("User or comment doesnt exist!"));
    }
}

package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CommentJdbcTemplateService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentJdbcTemplateServiceImpl implements CommentJdbcTemplateService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CommentResponse> getAllUserComments(Long userId) {
        String sqlQuery = """
                SELECT c.id                                    AS id,
                       c.comment                               AS comment,
                       c.created_date                          AS date,
                       u.id                                    AS creatorId,
                       concat(u.first_name, '  ', u.last_name) AS fullName,
                       u.image                                 AS avatar
                FROM comments AS c
                         JOIN users u ON u.id = c.user_id
                         LEFT JOIN cards_users cu
                                   ON u.id = cu.users_id
                WHERE user_id=?
                """;
        return jdbcTemplate.query(sqlQuery, new Object[]{userId}, new CommentResponseRowMapperr());
    }

    private class CommentResponseRowMapperr implements RowMapper<CommentResponse> {
        @Override
        public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(rs.getLong("id"));
            commentResponse.setComment(rs.getString("comment"));
            commentResponse.setCreatorName(rs.getString("fullName"));
            commentResponse.setCreatorId(rs.getLong("creatorId"));
            commentResponse.setAvatar(rs.getString("avatar"));
            java.sql.Timestamp timestamp = rs.getTimestamp("date");
            if (timestamp != null) {
                ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                String formattedDateTime = zonedDateTime.format(formatter);
                commentResponse.setCreatedDate(formattedDateTime);
            }
            return commentResponse;
        }
    }


    @Override
    public List<CommentResponse> getAllCommentByCardId(Long cardId) {
        String sqlQuery = """
                SELECT c.id                                     AS id,
                       c.comment                                AS comment,
                       c.created_date                           AS date,
                       u.id                                     AS creatorId,
                       concat(u.first_name, '   ', u.last_name) AS fullName,
                       u.image                                  AS image
                FROM comments AS c
                         JOIN users u ON u.id = c.user_id
                              
                  AND c.id = ?
                  """;
        return jdbcTemplate.query
                (sqlQuery, new Object[]{cardId}, new CommentResponseRowMapperer());
    }

    private class CommentResponseRowMapperer implements RowMapper<CommentResponse> {
        @Override
        public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(rs.getLong("id"));
            commentResponse.setComment(rs.getString("comment"));
            commentResponse.setCreatorName(rs.getString("fullName"));
            commentResponse.setCreatorId(rs.getLong("creatorId"));
            commentResponse.setAvatar(rs.getString("image"));
            java.sql.Timestamp timestamp = rs.getTimestamp("date");
            if (timestamp != null) {
                ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                String formattedDateTime = zonedDateTime.format(formatter);
                commentResponse.setCreatedDate(formattedDateTime);
            }
            return commentResponse;
        }
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return null;
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        String sqlQuery = """
                SELECT c.id                                     AS id,
                       c.comment                                AS comment,
                       c.created_date                           AS date,
                       u.id                                     AS creatorId,
                       concat(u.first_name, '   ', u.last_name) AS fullName,
                       u.image                                  AS image
                FROM comments AS c
                         JOIN users u ON u.id = c.user_id
                              
                  AND c.id = ?
                  """;
        return jdbcTemplate.queryForObject
                (sqlQuery, new Object[]{commentId}, new CommentResponseRowMapper());
    }

    private class CommentResponseRowMapper implements RowMapper<CommentResponse> {
        @Override
        public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(rs.getLong("id"));
            commentResponse.setComment(rs.getString("comment"));
            commentResponse.setCreatorName(rs.getString("fullName"));
            commentResponse.setCreatorId(rs.getLong("creatorId"));
            commentResponse.setAvatar(rs.getString("image"));
            java.sql.Timestamp timestamp = rs.getTimestamp("date");
            if (timestamp != null) {
                ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                String formattedDateTime = zonedDateTime.format(formatter);
                commentResponse.setCreatedDate(formattedDateTime);
            }
            return commentResponse;
        }
    }
}

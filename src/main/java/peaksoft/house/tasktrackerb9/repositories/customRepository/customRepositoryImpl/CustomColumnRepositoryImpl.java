package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomColumnRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomColumnRepositoryImpl implements CustomColumnRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JwtService jwtService;

    @Override
    public List<ColumnResponse> getAllColumns(Long boardId) {
        String sql = """
                SELECT c.id, c.title, c.is_archive, c.board_id  AS boardId, card.id AS card_id, card.title AS card_title
                FROM columns c
                LEFT JOIN cards card ON c.id = card.column_id
                WHERE c.board_id = ?
                ORDER BY c.id
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long columnId = rs.getLong("id");
            String title = rs.getString("title");
            Boolean isArchive = rs.getBoolean("is_archive");
            Long fetchedBoardId = rs.getLong("boardId");
            return new ColumnResponse(columnId, title, isArchive, fetchedBoardId, getAllCardsByColumnId(rs.getLong("id")));
        }, boardId);
    }
    public List<CardResponse> getAllCardsByColumnId(Long columnId) {
        String query = """
                SELECT c.id AS cardId,
                       c.title AS title,
                       e.start_date AS startDate,
                       e.due_date AS dueDate,
                       (SELECT COUNT(*) FROM cards_members AS cu WHERE cu.cards_id = c.id) AS numberUsers,
                       (SELECT COUNT(*) FROM items AS i
                       JOIN check_lists AS cl ON i.check_list_id = cl.id
                       WHERE i.is_done = true AND cl.card_id = c.id) AS numberCompletedItems,
                       (SELECT COUNT(*) FROM check_lists AS cl WHERE cl.card_id = c.id) AS numberItems
                FROM cards AS c
                LEFT JOIN estimations AS e ON c.id = e.card_id
                WHERE c.column_id = ? and c.is_archive = false
                """;

        List<CardResponse> cardResponses = jdbcTemplate.query(query, new Object[]{columnId}, (rs, rowNum) -> {
            CardResponse cardResponse = new CardResponse();
            cardResponse.setCardId(rs.getLong("cardId"));
            cardResponse.setTitle(rs.getString("title"));

            ZoneId zoneId = ZoneId.systemDefault();

            ZonedDateTime startDate = rs.getTimestamp("startDate") != null ? rs.getTimestamp("startDate").toLocalDateTime().atZone(zoneId) : null;
            ZonedDateTime dueDate = rs.getTimestamp("dueDate") != null ? rs.getTimestamp("dueDate").toLocalDateTime().atZone(zoneId) : null;

            if (startDate != null && dueDate != null) {
                Duration duration = Duration.between(startDate, dueDate);
                long days = duration.toDays();
                cardResponse.setDuration(days + " days");
            } else {
                cardResponse.setDuration("No time set for this card");
            }

            int numberUsers = rs.getInt("numberUsers");
            cardResponse.setNumberOfUsers(numberUsers);

            int numberItems = rs.getInt("numberItems");
            cardResponse.setNumberOfItems(numberItems);

            int numberCompletedItems = rs.getInt("numberCompletedItems");
            cardResponse.setNumberOfCompletedItems(numberCompletedItems);

            List<LabelResponse> labelResponses = getLabelResponsesForCard(rs.getLong("cardId"));
            cardResponse.setLabelResponses(labelResponses);

            List<CommentResponse> commentResponses = getCommentResponsesForCard(rs.getLong("cardId"));
            cardResponse.setCommentResponses(commentResponses);

            return cardResponse;
        });
        if (cardResponses.isEmpty()) {
            return new ArrayList<>();
        }
        return cardResponses;
    }


    private List<LabelResponse> getLabelResponsesForCard(Long cardId) {
        String sql = """
                SELECT l.id AS labelId,
                       l.label_name AS name,
                       l.color AS color 
                FROM labels AS l
                JOIN labels_cards lc ON l.id = lc.labels_id
                WHERE lc.cards_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LabelResponse labelResponse = new LabelResponse();
            labelResponse.setLabelId(rs.getLong("labelId"));
            labelResponse.setDescription(rs.getString("name"));
            labelResponse.setColor(rs.getString("color"));

            return labelResponse;
        }, cardId);
    }

    private List<CommentResponse> getCommentResponsesForCard(Long cardId) {
        User user = jwtService.getAuthentication();
        String query = """
                SELECT co.id AS commentId,
                       co.comment AS comment,
                       co.created_date AS createdDate,
                       u.id AS userId,
                       CONCAT(u.first_name, ' ', u.last_name) AS fullName,
                       CASE WHEN u.id = ? THEN TRUE ELSE FALSE END AS isMine,
                       u.image AS image
                FROM comments AS co
                JOIN cards c ON c.id = co.card_id
                JOIN users u ON co.member_id = u.id
                WHERE c.id = ?
                """;

        return jdbcTemplate.query(query, (rs, rowNum) -> {
                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setCommentId(rs.getLong("commentId"));
                    commentResponse.setComment(rs.getString("comment"));

                    Timestamp timestamp = rs.getTimestamp("createdDate");
                    if (timestamp != null) {
                        ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                        String formattedDateTime = zonedDateTime.format(formatter);
                        commentResponse.setCreatedDate(formattedDateTime);
                        commentResponse.setCreatorId(rs.getLong("userId"));
                        commentResponse.setCreatorName(rs.getString("fullName"));
                        commentResponse.setCreatorAvatar(rs.getString("image"));
                        commentResponse.setIsMyComment(rs.getBoolean("IsMine"));
                    }

                    return commentResponse;
                }
                , user.getId(),cardId);
    }

}

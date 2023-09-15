package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.*;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomBoardRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                " LEFT JOIN favorites f ON b.id = f.board_id AND f.member_id = ? "+
                " WHERE ws.id = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new BoardResponse(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("back_ground"),
                        rs.getBoolean("isFavorite")),
                        user.getId(),workSpaceId);
    }

    @Override
    public GetAllArchiveResponse getAllArchivedCardsAndColumns(Long boardId) {
        GetAllArchiveResponse getAllArchiveResponse = new GetAllArchiveResponse();
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
                          LEFT JOIN columns c2 ON c.column_id = c2.id
                         LEFT JOIN boards b ON b.id = c2.board_id
                WHERE b.id = ? AND c.is_archive = true GROUP BY c.id, c.title, e.start_date, e.due_date
                """;

        List<CardResponse> cardResponses = jdbcTemplate.query(query, new Object[]{boardId}, (rs, rowNum) -> {
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
            if(!labelResponses.isEmpty()){
                cardResponse.setLabelResponses(labelResponses);
            }else {
                cardResponse.setLabelResponses(new ArrayList<>());
            }
            List<CommentResponse> commentResponses = getCommentResponsesForCard(rs.getLong("cardId"));
            if(!labelResponses.isEmpty()){
                cardResponse.setCommentResponses(commentResponses);
            }else {
                cardResponse.setCommentResponses(new ArrayList<>());
            }
            return cardResponse;
        });

        String sql = """
            SELECT c.id, c.title, c.is_archive FROM boards b
            JOIN columns c ON b.id = c.board_id
            WHERE b.id = ? AND c.is_archive = true
            group by c.id, c.title, c.is_archive
            """;
        List<ColumnResponse> columnResponses = jdbcTemplate.query(sql,
                ((rs, rowNum) -> new ColumnResponse(rs.getLong("id"),
                rs.getString("title"),
                rs.getBoolean("is_archive"))), boardId);

        if (!cardResponses.isEmpty()){
            getAllArchiveResponse.setCardResponses(cardResponses);
        }else {
            getAllArchiveResponse.setCardResponses(new ArrayList<>());
        }
        if(!columnResponses.isEmpty()){
            getAllArchiveResponse.setColumnResponses(columnResponses);
        }else {
            getAllArchiveResponse.setColumnResponses(new ArrayList<>());
        }
        return getAllArchiveResponse;
    }

    private List<LabelResponse> getLabelResponsesForCard(Long cardId) {
        String sql1 = """
                 SELECT l.id AS labelId,
                        l.label_name AS name,
                        l.color AS color 
                 FROM labels AS l
                 JOIN labels_cards lc ON l.id = lc.labels_id
                 WHERE lc.cards_id = ?
                 """;
        return jdbcTemplate.query(sql1, (rs, rowNum) -> {
            LabelResponse labelResponse = new LabelResponse();
            labelResponse.setLabelId(rs.getLong("labelId"));
            labelResponse.setDescription(rs.getString("name"));
            labelResponse.setColor(rs.getString("color"));

            return labelResponse;
        }, cardId);
    }

    private List<CommentResponse> getCommentResponsesForCard(Long cardId) {
        User user = jwtService.getAuthentication();
        String query1 = """
                   SELECT co.id AS commentId,
                          co.comment AS comment,
                          co.created_date AS createdDate,
                          u.id AS userId,
                          CONCAT(u.first_name, ' ', u.last_name) AS fullName,
                          u.image AS image,
                          CASE WHEN u.id = ? THEN TRUE ELSE FALSE END AS isMine
                   FROM comments AS co
                   JOIN cards c ON c.id = co.card_id
                   JOIN users u ON co.member_id = u.id
                   WHERE c.id = ?
                   """;

        return jdbcTemplate.query(query1, (rs, rowNum) -> {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(rs.getLong("commentId"));
            commentResponse.setComment(rs.getString("comment"));
            commentResponse.setIsMyComment(rs.getBoolean("isMine"));

            Timestamp timestamp = rs.getTimestamp("createdDate");
            if (timestamp != null) {
                ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                String formattedDateTime = zonedDateTime.format(formatter);
                commentResponse.setCreatedDate(formattedDateTime);
                commentResponse.setCreatorId(rs.getLong("userId"));
                commentResponse.setCreatorName(rs.getString("fullName"));
                commentResponse.setCreatorAvatar(rs.getString("image"));
            }
            return commentResponse;
        },user.getId(), cardId);
    }
}
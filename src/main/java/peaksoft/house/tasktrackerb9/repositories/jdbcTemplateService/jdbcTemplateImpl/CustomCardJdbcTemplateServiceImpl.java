package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.*;
import peaksoft.house.tasktrackerb9.enums.ReminderType;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CustomCardJdbcTemplateService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomCardJdbcTemplateServiceImpl implements CustomCardJdbcTemplateService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public CardInnerPageResponse getAllCardInnerPage(Long cardId) {

        String query = """
                     SELECT c.id AS cardId,
                            c.title AS title,
                            c.description AS description,
                            c.is_archive AS isArchive                          
                            FROM cards AS c
                            WHERE c.id = ?
                                 """;

        List<CardInnerPageResponse> cardInnerPageResponses = jdbcTemplate.query(query, new Object[]{cardId}, (rs, rowNum) -> {
            CardInnerPageResponse cardInnerPageResponse1 = new CardInnerPageResponse();
            cardInnerPageResponse1.setCardId(rs.getLong("cardId"));
            cardInnerPageResponse1.setTitle(rs.getString("title"));
            cardInnerPageResponse1.setDescription(rs.getString("description"));
            cardInnerPageResponse1.setIsArchive(rs.getBoolean("isArchive"));

            EstimationResponse estimationResponse = getEstimationByCardId(rs.getLong("cardId"));
            cardInnerPageResponse1.setEstimationResponse(estimationResponse);

            List<LabelResponse> labelResponses = getLabelResponsesByCardId(rs.getLong("cardId"));
            cardInnerPageResponse1.setLabelResponses(labelResponses);

            List<CheckListResponse> checkListResponses = getCheckListResponsesByCardId(rs.getLong("cardId"));
            cardInnerPageResponse1.setChecklistResponses(checkListResponses);

            List<UserResponse> userResponses = getMembersResponsesByCardId(rs.getLong("cardId"));
            cardInnerPageResponse1.setUserResponses(userResponses);

            List<CommentResponse> commentResponses = getCommentsResponsesByCardId(rs.getLong("cardId"));
            cardInnerPageResponse1.setCommentResponses(commentResponses);

            return cardInnerPageResponse1;

        });



        if (cardInnerPageResponses.isEmpty()) {
            log.error("Card with id: " + cardId + " not found!");
            throw new NotFoundException("Card with id: " + cardId + " not found!");
        }
        return cardInnerPageResponses.get(0);

    }


    private EstimationResponse getEstimationByCardId(Long cardId) {

        String sql = """
                        SELECT e.id AS estimationId,
                               e.start_date AS startDate,
                               e.due_date AS dueDate,
                               e.time AS time,
                               e.reminder_type AS reminderType
                               FROM cards AS ca JOIN estimations AS e ON ca.id = e.card_id
                               WHERE ca.id = ?
                    """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{cardId}, (rs, rowNum) -> {
                EstimationResponse estimationResponse = new EstimationResponse();
                estimationResponse.setEstimationId(rs.getLong("estimationId"));

                Timestamp startDateTimestamp = rs.getTimestamp("startDate");
                if (startDateTimestamp != null) {
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime startDateZoned = startDateTimestamp.toInstant().atZone(zoneId);
                    String formattedStartDate = startDateZoned.format(DateTimeFormatter.ofPattern("d MMM yyyy 'at' h:mm a"));
                    estimationResponse.setStartDate(formattedStartDate);
                }

                Timestamp dueDateTimestamp = rs.getTimestamp("dueDate");
                if (dueDateTimestamp != null) {
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime dueDateZoned = dueDateTimestamp.toInstant().atZone(zoneId);
                    String formattedDueDate = dueDateZoned.format(DateTimeFormatter.ofPattern("d MMM yyyy 'at' h:mm a"));
                    estimationResponse.setDuetDate(formattedDueDate);
                }

                OffsetDateTime timeOffset = rs.getObject("time", OffsetDateTime.class);
                if (timeOffset != null) {
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime timeZoned = timeOffset.toInstant().atZone(zoneId);
                    String formattedTime = timeZoned.format(DateTimeFormatter.ofPattern("h:mm a"));
                    estimationResponse.setTime(formattedTime);
                }

                String reminderTypeStr = rs.getString("reminderType");
                if (reminderTypeStr != null) {
                    try {
                        estimationResponse.setReminderType(ReminderType.valueOf(reminderTypeStr));
                    } catch (BadCredentialException e) {
                        throw new BadCredentialException("invalid reminderType");
                    }
                }
                return estimationResponse;
            });
        } catch (NotFoundException e) {
            throw new NotFoundException("Estimation not found!");
        }

    }

    private List<LabelResponse> getLabelResponsesByCardId(Long cardId) {

        String sql = """
                    SELECT l.id AS labelId,
                    l.label_name AS label_name,
                    l.color AS color
                    FROM labels AS l
                    JOIN labels_cards lc ON l.id = lc.labels_id
                    WHERE lc.cards_id = ?
                    """;

        List<LabelResponse> labelResponses = jdbcTemplate.query(sql, new Object[]{cardId}, (rs, rowNum) -> {
            LabelResponse labelResponse = new LabelResponse();
            labelResponse.setLabelId(rs.getLong("labelId"));
            labelResponse.setDescription(rs.getString("label_name"));
            labelResponse.setColor(rs.getString("color"));

            return labelResponse;
        });
        return labelResponses;
    }

    private List<CheckListResponse> getCheckListResponsesByCardId(Long cardId) {

        String sql = """
                    SELECT cl.id AS checkListId,
                           cl.description AS description,
                           cl.percent AS percent
                           FROM check_lists AS cl
                           JOIN cards c ON c.id = cl.card_id
                           WHERE c.id = ?
                    """;

        List<CheckListResponse> checkListResponses = jdbcTemplate.query(sql, new Object[]{cardId}, (rs, rowNum) -> {
            CheckListResponse checkListResponse = new CheckListResponse();
            checkListResponse.setCheckListId(rs.getLong("checkListId"));
            checkListResponse.setDescription(rs.getString("description"));
            checkListResponse.setPercent(rs.getInt("percent"));

            return checkListResponse;
        });
        return checkListResponses;
    }

    private List<UserResponse> getMembersResponsesByCardId(Long cardId) {

        String sql = """
              SELECT u.id AS memberId,
                     u.first_name AS firstName,
                     u.last_name AS lastName,
                     u.email AS email,
                     u.image AS image
                     FROM users AS u 
                     JOIN cards_users cu ON u.id = cu.users_id 
                     WHERE cu.cards_id = ?
              """;

        List<UserResponse> userResponses = jdbcTemplate.query(sql, new Object[]{cardId}, (rs, rowNum) -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(rs.getLong("memberId"));
            userResponse.setFirstName(rs.getString("firstName"));
            userResponse.setLastName(rs.getString("lastName"));
            userResponse.setEmail(rs.getString("email"));
            userResponse.setAvatar(rs.getString("image"));

            return userResponse;
        });
        return userResponses;
    }

    private List<CommentResponse> getCommentsResponsesByCardId(Long cardId){

        String sql = """
                 SELECT co.id AS commentId,
                        co.comment AS comment,
                        co.created_date AS created_date,                       
                        u.id AS user_id,
                        u.first_name AS firstName,
                        u.last_name AS lastName,
                        u.image AS image
                        FROM comments AS co
                        JOIN cards c ON c.id = co.card_id
                        JOIN users u ON co.user_id = u.id
                        WHERE c.id = ?
                 """;

        List<CommentResponse> commentResponses = jdbcTemplate.query(sql, new Object[]{cardId}, (rs, rowNum) -> {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(rs.getLong("commentId"));
            commentResponse.setComment(rs.getString("comment"));

            Timestamp timestamp = rs.getTimestamp("created_date");
            if (timestamp != null) {
                ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                String formattedDateTime = zonedDateTime.format(formatter);
                commentResponse.setCreatedDate(formattedDateTime);
            }
            CommentUserResponse userResponse = new CommentUserResponse();
            userResponse.setUserId(rs.getLong("user_id"));
            userResponse.setFirstName(rs.getString("firstName"));
            userResponse.setLastName(rs.getString("lastName"));
            userResponse.setImage(rs.getString("image"));
            commentResponse.setCommentUserResponse(userResponse);

            return commentResponse;
        });

        return commentResponses;
    }

    @Override
    public List<CardResponse> getAllCardsByColumnId(Long columnId) {

        String query = """
            SELECT c.id AS cardId,
                   c.title AS title,
                   e.start_date AS startDate,
                   e.due_date AS dueDate,
                   (SELECT COUNT(*) FROM cards_users AS cu WHERE cu.cards_id = c.id) AS numberUsers,
                   (SELECT COUNT(*) FROM items AS i
                   JOIN check_lists AS cl ON i.check_list_id = cl.id
                   WHERE i.is_done = true AND cl.card_id = c.id) AS numberCompletedItems,
                   (SELECT COUNT(*) FROM check_lists AS cl WHERE cl.card_id = c.id) AS numberItems
                   FROM cards AS c
                   LEFT JOIN estimations AS e ON c.id = e.card_id
                   WHERE c.column_id = ? 
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
            cardResponse.setNumberUsers(numberUsers);

            int numberItems = rs.getInt("numberItems");
            cardResponse.setNumberItems(numberItems);

            int numberCompletedItems = rs.getInt("numberCompletedItems");
            cardResponse.setNumberCompletedItems(numberCompletedItems);

            List<LabelResponse> labelResponses = getLabelResponsesForCard(rs.getLong("cardId"));
            cardResponse.setLabelResponses(labelResponses);

            List<CommentResponse> commentResponses = getCommentResponsesForCard(rs.getLong("cardId"));
            cardResponse.setCommentResponses(commentResponses);

            return cardResponse;
        });

        if (cardResponses.isEmpty()) {
            throw new NotFoundException("Column with id: " + columnId + " not found!");
        } else {
            return cardResponses;
        }
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
        List<LabelResponse> labelResponses = jdbcTemplate.query(sql, new Object[]{cardId}, (rs, rowNum) -> {
            LabelResponse labelResponse = new LabelResponse();
            labelResponse.setLabelId(rs.getLong("labelId"));
            labelResponse.setDescription(rs.getString("name"));
            labelResponse.setColor(rs.getString("color"));

            return labelResponse;
        });
        return labelResponses;
    }

    private List<CommentResponse> getCommentResponsesForCard(Long cardId) {

        String query = """
                       SELECT co.id AS commentId,
                              co.comment AS comment,
                              co.created_date AS createdDate,
                              u.id AS userId,
                              u.first_name AS firstName,
                              u.last_name AS lastName,
                              u.image AS image
                              FROM comments AS co
                              JOIN cards c ON c.id = co.card_id
                              JOIN users u ON co.user_id = u.id
                              WHERE c.id = ?
                       """;
        List<CommentResponse> commentResponses = jdbcTemplate.query(query, new Object[]{cardId}, (rs, rowNum) -> {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(rs.getLong("commentId"));
            commentResponse.setComment(rs.getString("comment"));
            Timestamp timestamp = rs.getTimestamp("createdDate");
            if (timestamp != null) {
                ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy / h:mma");
                String formattedDateTime = zonedDateTime.format(formatter);
                commentResponse.setCreatedDate(formattedDateTime);

                CommentUserResponse userResponse = new CommentUserResponse();
                userResponse.setUserId(rs.getLong("userId"));
                userResponse.setFirstName(rs.getString("firstName"));
                userResponse.setLastName(rs.getString("lastName"));
                userResponse.setImage(rs.getString("image"));

                commentResponse.setCommentUserResponse(userResponse);
            }
            return commentResponse;
        });
        return commentResponses;
    }
}
package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.*;
import peaksoft.house.tasktrackerb9.enums.NotificationType;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomNotificationRepository;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JwtService jwtService;

    @Override
    public List<NotificationResponse> getAllNotifications() {
        String sql = """
                select n.id                                   as notificationId,
                       n.text                                 as text,
                       n.created_date                         as createdDate,
                       n.is_read                              as isRead,
                       n.type                                 as type,
                       u.id                                   as fromUserId,
                       concat(u.first_name, ' ', u.last_name) as fullName,
                       u.image                                as imageUser,
                       b.id                                   as boardId,
                       b.title                                as titleBoard,
                       b.back_ground                          as backGround,
                       c.id                                   as columnId,
                       c.title                                as columnName,
                       c2.id                                  as cardId
                from notifications as n
                         left join boards b on b.id = n.board_id
                         left join columns c on c.id = n.column_id
                         left join users u on n.from_user_id = u.id
                         left join cards c2 on c2.id = n.card_id
                group by n.id, n.text, n.created_date, n.is_read, n.type, u.id,
                         concat(u.first_name, ' ', u.last_name), u.image, b.id, b.title,
                         b.back_ground, c.id, c.title, c2.id;""";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            NotificationResponse response = new NotificationResponse();
            response.setNotificationId(rs.getLong("notificationId"));
            response.setText(rs.getString("text"));
            response.setCreatedDate(rs.getString("createdDate"));
            response.setIsRead(rs.getBoolean("isRead"));
            response.setNotificationType(NotificationType.valueOf(rs.getString("type")));
            response.setFromUserId(rs.getLong("fromUserId"));
            response.setFullName(rs.getString("fullName"));
            response.setImageUser(rs.getString("imageUser"));
            response.setBoardId(rs.getLong("boardId"));
            response.setTitleBoard(rs.getString("titleBoard"));
            response.setBackGround(rs.getString("backGround"));
            response.setColumnId(rs.getLong("columnId"));
            response.setColumnName(rs.getString("columnName"));
            response.setCardId(rs.getLong("cardId"));

            return response;
        });
    }

    @Override
    public NotificationResponse getNotificationById(Long notificationId) {
        String sql = """
          select n.id                                   as notificationId,
                 n.text                                 as text,
                 n.created_date                         as createdDate,
                 n.is_read                              as isRead,
                 n.type                                 as type,
                 u.id                                   as fromUserId,
                 concat(u.first_name, ' ', u.last_name) as fullName,
                 u.image                                as imageUser,
                 b.id                                   as boardId,
                 b.title                                as titleBoard,
                 b.back_ground                          as backGround,
                 c.id                                   as columnId,
                 c.title                                as columnName,
                 c2.id                                  as cardId
          from notifications as n
                   left join boards b on b.id = n.board_id
                   left join columns c on c.id = n.column_id
                   left join users u on n.from_user_id = u.id
                   left join cards c2 on c2.id = n.card_id where n.id = ?
          group by n.id, n.text, n.created_date, n.is_read, n.type, u.id,
                   concat(u.first_name, ' ', u.last_name), u.image, b.id, b.title,
                   b.back_ground, c.id, c.title, c2.id;""";

        List<NotificationResponse> notificationResponses = jdbcTemplate.query(
                sql,
                new Object[]{notificationId},
                (rs, rowNum) -> {
                    NotificationResponse response = new NotificationResponse();
                    response.setNotificationId(rs.getLong("notificationId"));
                    response.setText(rs.getString("text"));
                    response.setCreatedDate(rs.getString("createdDate"));
                    response.setIsRead(rs.getBoolean("isRead"));
                    response.setNotificationType(NotificationType.valueOf(rs.getString("type")));
                    response.setFromUserId(rs.getLong("fromUserId"));
                    response.setFullName(rs.getString("fullName"));
                    response.setImageUser(rs.getString("imageUser"));
                    response.setBoardId(rs.getLong("boardId"));
                    response.setTitleBoard(rs.getString("titleBoard"));
                    response.setBackGround(rs.getString("backGround"));
                    response.setColumnId(rs.getLong("columnId"));
                    response.setColumnName(rs.getString("columnName"));
                    response.setCardId(rs.getLong("cardId"));

                    return response;
                });
            return notificationResponses.get(0);
    }

}

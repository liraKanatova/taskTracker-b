package peaksoft.house.tasktrackerb9.dto.response;

import lombok.*;
import peaksoft.house.tasktrackerb9.enums.NotificationType;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;
    private Long boardId;
    private Long cardId;
    private Long columnId;
    private Long fromUserId;
    private String titleBoard;
    private String backGround;
    private String columnName;
    private String fullName;
    private String imageUser;
    private String text;
    private String createdDate;
    private Boolean isRead;
    private NotificationType notificationType;

}
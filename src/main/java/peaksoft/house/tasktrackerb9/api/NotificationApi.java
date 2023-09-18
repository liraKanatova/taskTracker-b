package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.NotificationResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Notification API", description = "API for managing notifications")
public class NotificationApi {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get all", description = "Get all notifications")
    public List<NotificationResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = "Get by id", description = "Get notification by notification id")
    public NotificationResponse getById(@PathVariable Long notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

    @PutMapping
    @Operation(summary = "Mark notifications as read", description = "Marks all user notifications as read.")
    public SimpleResponse markAsRead() {
        return notificationService.markAsRead();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mark notifications as read", description = "Marks user by id notifications as read.")
    public SimpleResponse markAsReadById(@PathVariable Long id) {
        return notificationService.markAsReadById(id);
    }
}

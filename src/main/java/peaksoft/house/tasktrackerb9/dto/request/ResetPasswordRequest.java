package peaksoft.house.tasktrackerb9.dto.request;

public record ResetPasswordRequest(
        Long userId,
        String newPassword,
        String repeatPassword) {
}

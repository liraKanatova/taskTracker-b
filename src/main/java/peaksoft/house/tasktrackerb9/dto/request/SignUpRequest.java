package peaksoft.house.tasktrackerb9.dto.request;

public record SignUpRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}

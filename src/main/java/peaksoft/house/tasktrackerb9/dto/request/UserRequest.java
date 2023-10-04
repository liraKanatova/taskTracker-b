package peaksoft.house.tasktrackerb9.dto.request;

import jakarta.persistence.Column;
import lombok.Builder;
import peaksoft.house.tasktrackerb9.validation.EmailValid;

@Builder
public record UserRequest(
        String firstName,
        String lastName,

        @Column(unique = true)
        @EmailValid
        String email,
        String password
) {
}
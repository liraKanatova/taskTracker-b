package peaksoft.house.tasktrackerb9.dto.request;

import jakarta.persistence.Column;
import peaksoft.house.tasktrackerb9.validation.EmailValid;
import peaksoft.house.tasktrackerb9.validation.PasswordValid;

public record SignInRequest(
        
        @Column(unique = true)
        @EmailValid
        String email,
        @PasswordValid
        String password
) {
}

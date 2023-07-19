package peaksoft.house.tasktrackerb9.dto.response;

import lombok.Builder;
import peaksoft.house.tasktrackerb9.enums.Role;

@Builder
public record ResetPasswordResponse(Long id,
                                    String firstName,
                                    String lastName,
                                    String email,
                                    Role role,
                                    String jwtToken,
                                    String message) {
}
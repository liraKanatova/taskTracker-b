package peaksoft.house.tasktrackerb9.dto.request;

//import peaksoft.house.tasktrackerb9.validation.PasswordValid;
public record ResetPasswordRequest(Long userId,
//                                    @PasswordValid
                                    String newPassword,
//                                    @PasswordValid
                                    String repeatPassword) {
}

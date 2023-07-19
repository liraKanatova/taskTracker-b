package peaksoft.house.tasktrackerb9.api;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.SignInRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignUpRequest;
import peaksoft.house.tasktrackerb9.dto.response.AuthenticationResponse;
import peaksoft.house.tasktrackerb9.dto.response.ResetPasswordResponse;
import peaksoft.house.tasktrackerb9.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationApi {

    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public AuthenticationResponse signUp(@RequestBody SignUpRequest signUpRequest){
        return authenticationService.signUp(signUpRequest);
    }


    @PostMapping("/signIn")
    public  AuthenticationResponse signIn(@RequestBody SignInRequest signInRequest){
        return authenticationService.signIn(signInRequest);
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestParam String email, @RequestParam String link) throws MessagingException {
        authenticationService.forgotPassword(email, link);
    }

    @Operation(summary = "Reset password", description = "Allows you to reset the user's password")
    @PostMapping("/reset/password/{userId}")
    public ResetPasswordResponse resetPassword(@PathVariable Long userId, @RequestParam String newPassword, @RequestParam String repeatPassword) {
        return authenticationService.resetPassword(userId, newPassword, repeatPassword);
    }

    @Operation(summary = "Google authentication", description = "Any user can authenticate with Google")
    @PostMapping("/google")
    public AuthenticationResponse authWithGoogleAccount(@RequestParam String tokenId) throws FirebaseAuthException {
        return authenticationService.authWithGoogle(tokenId);
    }

}

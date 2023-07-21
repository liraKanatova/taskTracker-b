package peaksoft.house.tasktrackerb9.api;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.ResetPasswordRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignInRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignUpRequest;
import peaksoft.house.tasktrackerb9.dto.response.AuthenticationResponse;
import peaksoft.house.tasktrackerb9.dto.response.ResetPasswordResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Api",description = "Authorization for all users")
@CrossOrigin(origins = "*",maxAge = 3600)
public class AuthenticationApi {

    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    @Operation(summary = "SignUp",description = "User can register with signUp")
    public AuthenticationResponse signUp(@RequestBody SignUpRequest signUpRequest){
        return authenticationService.signUp(signUpRequest);
    }

    @PostMapping("/signIn")
    @Operation(summary = "SignIn",description = "Only registers users can login")
    public  AuthenticationResponse signIn(@RequestBody SignInRequest signInRequest){
        return authenticationService.signIn(signInRequest);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "If you have forgotten your password, enter gmail here")
    public SimpleResponse forgotPassword(@RequestParam String email, @RequestParam String link) throws MessagingException {
        return authenticationService.forgotPassword(email, link);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Here you can reset your password")
    public ResetPasswordResponse resetPassword(@RequestBody ResetPasswordRequest passwordRequest) {
        return authenticationService.resetPassword(passwordRequest);
    }

    @PostMapping("/google")
    @Operation(summary = "Google authentication", description = "All users can login with Google")
    public AuthenticationResponse authWithGoogleAccount(@RequestParam String tokenId) throws FirebaseAuthException {
        return authenticationService.authWithGoogle(tokenId);
    }

}
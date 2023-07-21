package peaksoft.house.tasktrackerb9.service;

import com.google.firebase.auth.FirebaseAuthException;
import jakarta.mail.MessagingException;
import peaksoft.house.tasktrackerb9.dto.request.ResetPasswordRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignInRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignUpRequest;
import peaksoft.house.tasktrackerb9.dto.response.AuthenticationResponse;
import peaksoft.house.tasktrackerb9.dto.response.ResetPasswordResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

public interface AuthenticationService {

    AuthenticationResponse signUp(SignUpRequest signUpRequest);

    AuthenticationResponse signIn(SignInRequest signInRequest);

    ResetPasswordResponse resetPassword(ResetPasswordRequest passwordRequest);

    SimpleResponse forgotPassword(String email, String link) throws MessagingException;

    AuthenticationResponse authWithGoogle(String tokenId) throws FirebaseAuthException;

}
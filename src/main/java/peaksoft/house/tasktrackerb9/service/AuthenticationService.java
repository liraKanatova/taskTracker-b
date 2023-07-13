package peaksoft.house.tasktrackerb9.service;


import peaksoft.house.tasktrackerb9.dto.request.SignInRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignUpRequest;
import peaksoft.house.tasktrackerb9.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse signUp(SignUpRequest signUpRequest);
    AuthenticationResponse signIn(SignInRequest signInRequest);

}

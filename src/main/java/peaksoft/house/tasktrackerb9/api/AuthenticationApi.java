package peaksoft.house.tasktrackerb9.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peaksoft.house.tasktrackerb9.dto.request.SignInRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignUpRequest;
import peaksoft.house.tasktrackerb9.dto.response.AuthenticationResponse;
import peaksoft.house.tasktrackerb9.services.AuthenticationService;

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
}

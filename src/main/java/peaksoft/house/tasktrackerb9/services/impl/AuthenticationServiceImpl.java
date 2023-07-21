package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.SignInRequest;
import peaksoft.house.tasktrackerb9.dto.request.SignUpRequest;
import peaksoft.house.tasktrackerb9.dto.response.AuthenticationResponse;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.services.AuthenticationService;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            log.error(String.format("User with email: %s already exist!", signUpRequest.email()));
            throw new EntityExistsException(String.format("User with email: %s already exist!", signUpRequest.email()));
        }
        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setImage("Default image");
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        if (signInRequest.email().isBlank()) {
            log.error("User with email: " + signInRequest.email() + " not found");
            throw new BadCredentialException("User with email: " + signInRequest.email() + " not found");
        }
        User user = userRepository.getUserByEmail(signInRequest.email()).orElseThrow(() -> {
            log.error("User with email: " + signInRequest.email() + " not found");
            return new NotFoundException("User with email: " + signInRequest.email() + " not found");

        });
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.error("Incorrect password !");
            throw new BadCredentialException("Incorrect password !");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwtToken)
                .build();

    }
}

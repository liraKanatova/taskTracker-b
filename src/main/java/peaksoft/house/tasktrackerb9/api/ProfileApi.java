package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile Api",description = "Api Profile to management")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ProfileApi {

    private final UserServiceImpl userService;


    @PutMapping
    @Operation(summary = "Update", description = "User updating profile user")
    public UserResponse updateUserBy(@RequestBody @Valid UserRequest userRequest) {
        return userService.updateUser(userRequest);
    }

    @PutMapping("/avatar")
    @Operation(summary = "Update image", description = "User updating profile image")
    public UserResponse updateImage( @RequestBody String userRequestImage) {
        return userService.updateImageUserId(userRequestImage);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Profile",description = "Profile user get by id")
    public ProfileResponse getProfileById(@PathVariable  Long userId){
        return userService.getProfileById(userId);
    }

    @GetMapping
    @Operation(summary ="My Profile",description = "Get by authentication")
    public UserResponse getMyProfile(){
        return userService.getMyProfile();
    }

}

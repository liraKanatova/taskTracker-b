package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
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
    public SimpleResponse updateUserBy(@RequestBody @Valid UserRequest userRequest) {
        return userService.updateUserBy( userRequest);
    }

    @PutMapping("/avatar/{userId}")
    @Operation(summary = "Update image", description = "User updating profile image")
    public SimpleResponse updateImage(@PathVariable  Long userId, @RequestBody String userRequestImage) {
        return userService.updateImageUserId(userId,userRequestImage);
    }

    @GetMapping("/profile/{userId}")
    @Operation(summary = "Profile",description = "User profile get by id")
    public ProfileResponse getProfileById(@PathVariable  Long userId){
        return userService.getProfileById(userId);
    }

}

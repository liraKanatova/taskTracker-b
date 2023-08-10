package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.GlobalSearchResponse;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.services.impl.ProfileServiceImpl;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile Api",description = "Api Profile to management")
@CrossOrigin(origins = "*",maxAge = 3600)
@PreAuthorize("hasAuthority('ADMIN')")
public class ProfileApi {

    private final ProfileServiceImpl userService;

    @PutMapping
    @Operation(summary = "Update", description = "User updating profile user")
    public UserResponse updateUserBy(@RequestBody @Valid UserRequest userRequest) {
        return userService.updateUser(userRequest);
    }

    @PutMapping("/avatar")
    @Operation(summary = "Update image", description = "User updating profile image")
    public UserResponse updateImage(@RequestBody String userRequestImage) {
        return userService.updateImageUserId(userRequestImage);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Profile",description = "Profile user get by id")
    public ProfileResponse getProfileById(@PathVariable Long userId){
        return userService.getProfileById(userId);
    }


    @GetMapping("/me")
    @Operation(summary = "My profile",description = "Get my profile")
    public UserResponse getMyProfile(){
        return userService.getMyProfile();
    }

    @GetMapping("/global-search")
    public GlobalSearchResponse search(@RequestParam  String search){
        return userService.search(search);
    }

}

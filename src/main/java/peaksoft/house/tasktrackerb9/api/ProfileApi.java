package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.response.ProfileResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/profile/users")
@RequiredArgsConstructor
@Tag(name = "Profile Api",description = "Api Profile to management")
@CrossOrigin(value = "*",maxAge = 3600)
public class ProfileApi {

    private final UserServiceImpl userService;


    @PutMapping("/updated")
    @Operation(summary = "Update user", description = "token")
    public SimpleResponse updateUserBy(@RequestBody UserRequest userRequest) {
        return userService.updateUserBy( userRequest);
    }

    @PutMapping("/image/{id}")
    @Operation(summary = "Update image", description = "token")
    public SimpleResponse updateImage(@PathVariable Long id, @RequestBody String userRequestImage) {
        return userService.updateImageUserId(id,userRequestImage);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get by id user",description = "token")
    public UserResponse getById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get by profile id",description = "token")
    public ProfileResponse getProfileById(@PathVariable Long id){
        return userService.getProfileById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id",description = "token")
    public SimpleResponse removeUserById(@PathVariable Long id){
        return userService.removeProfileUser(id);
    }
}

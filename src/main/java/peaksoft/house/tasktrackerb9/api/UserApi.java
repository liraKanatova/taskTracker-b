package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.UserRequest;
import peaksoft.house.tasktrackerb9.dto.request.UserRequestImage;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserResponse;
import peaksoft.house.tasktrackerb9.entity.WorkSpace;
import peaksoft.house.tasktrackerb9.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApi {

    private final UserServiceImpl userService;

    @PermitAll
    @GetMapping
    @Operation(summary = "Get all user", description = "token")
    public List<WorkSpace> getAll() {
        return userService.userGetAllWorkSpace();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("{id}")
    @Operation(summary = "Update user", description = "token")
    public SimpleResponse updateUserById(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return userService.updateUserById(id, userRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("image/{id}")
    @Operation(summary = "Update image", description = "token")
    public SimpleResponse updateImage(@PathVariable Long id, @RequestBody UserRequestImage userRequestImage) {
        return userService.updateImageUserId(id, userRequestImage);
    }

    @PermitAll
    @GetMapping("{id}")
    @Operation(summary = "Get by id user",description = "token")
    public UserResponse getById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("{id}")
    @Operation(summary = "Delete user by id",description = "token")
    public SimpleResponse deleteUserById(@PathVariable Long id){
        return userService.deleteProfileUser(id);
    }
}

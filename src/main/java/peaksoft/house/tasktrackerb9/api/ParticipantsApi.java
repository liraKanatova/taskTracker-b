package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsChangeRequest;
import peaksoft.house.tasktrackerb9.dto.request.ParticipantsRequest;
import peaksoft.house.tasktrackerb9.dto.response.ParticipantsResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.ParticipantsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participants")
@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Participants api", description = "Api participants to management")
public class ParticipantsApi {

    private final ParticipantsService participantsService;

    @Operation(summary = "Invite participants",description = "Invite participants to workSpace id")
    @PostMapping
    public SimpleResponse inviteToWorkSpaces(@RequestBody ParticipantsRequest participantsRequest) throws MessagingException {
        return participantsService.inviteToWorkSpaces(participantsRequest);
    }

    @Operation(summary = "Change role update", description = "Participants change role update to workspace id")
    @PutMapping
    public SimpleResponse changeUpdateRole(@RequestBody ParticipantsChangeRequest request) {
        return participantsService.changeUpdateRole(request);
    }

    @Operation(summary = "Get all participants", description = "Get all participants to workSpace id")
    @GetMapping("/getAll/{workSpacesId}")
    public List<ParticipantsResponse> getAllParticipants(@PathVariable Long workSpacesId) {
        return participantsService.getAllParticipants(workSpacesId);
    }

    @Operation(summary = "Get all admin", description = "Get all participants admin to workSpace id")
    @GetMapping("/getAll-Admin/{workSpacesId}")
    public List<ParticipantsResponse> getAllAdminParticipants(@PathVariable Long workSpacesId) {
        return participantsService.getAllAdminParticipants(workSpacesId);
    }

    @Operation(summary = "Get all member", description = "Get all participants member to workSpace id")
    @GetMapping("/getAll-Member/{workSpacesId}")
    public List<ParticipantsResponse> getAllMemberParticipants(@PathVariable Long workSpacesId) {
        return participantsService.getAllMemberParticipants(workSpacesId);
    }

    @DeleteMapping("/remove/{workSpacesId}/{userId}")
    public SimpleResponse removeParticipants(@PathVariable Long workSpacesId, @PathVariable Long userId) {
        return participantsService.removeToWorkSpaces(workSpacesId, userId);
    }

}

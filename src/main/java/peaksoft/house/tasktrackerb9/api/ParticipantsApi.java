package peaksoft.house.tasktrackerb9.api;

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

    @PostMapping("/invite")
    public SimpleResponse inviteToWorkSpaces(@RequestBody ParticipantsRequest participantsRequest) throws MessagingException {
        return participantsService.inviteToWorkSpaces(participantsRequest);
    }
    @PutMapping("/changeUpdate")
    public SimpleResponse changeUpdateRole(@RequestBody ParticipantsChangeRequest request){
        return participantsService.changeUpdateRole(request);
    }

    @GetMapping("/getAll/{workSpacesId}")
    public List<ParticipantsResponse>getAllParticipants(@PathVariable Long workSpacesId){
        return participantsService.getAllParticipants(workSpacesId);
    }

    @GetMapping("/getAllAdmin/{workSpacesId}")
    public List<ParticipantsResponse>getAllAdminParticipants(@PathVariable Long workSpacesId){
        return participantsService.getAllAdminParticipants(workSpacesId);
    }
    @GetMapping("/getAllMember/{workSpacesId}")
    public List<ParticipantsResponse>getAllMemberParticipants(@PathVariable Long workSpacesId){
        return participantsService.getAllMemberParticipants(workSpacesId);
    }
    @DeleteMapping("/remove/{workSpacesId}/{userId}")
    public SimpleResponse removeParticipants(@PathVariable Long workSpacesId,@PathVariable Long userId){
        return participantsService.removeToWorkSpaces(workSpacesId,userId);
    }

}

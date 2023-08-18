package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.services.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Member Api", description = "Api methods for members")
public class MemberApi {

    private final MemberService memberService;

    @GetMapping("/search")
    @Operation(summary = "Search ", description = "User search members by email or name")
    public List<MemberResponse> searchUserByEmailOrName(@RequestParam(name = "workSpaceId") Long workSpaceId, @RequestParam(name = "email") String email) {
        return memberService.searchByEmail(workSpaceId, email);
    }

    @GetMapping("/{cardId}")
    @Operation(summary = "All members", description = "Get all members by board and work_space")
    public AllMemberResponse getAll(@PathVariable Long cardId) {
        return memberService.getAll(cardId);
    }
}

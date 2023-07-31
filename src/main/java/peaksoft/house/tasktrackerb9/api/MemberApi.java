package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.services.MemberService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Tag(name = "Member Api", description = "Api methods for members")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MemberApi {

private final MemberService memberService;

@GetMapping("/search/{workSpaceId}")
    @Operation(summary = "Search members",description = "User search members by email or name")
    public List<MemberResponse> searchUserByEmailOrName(@PathVariable Long workSpaceId, @RequestParam String word){
    return memberService.searchMemberByWord(workSpaceId, word);
}

@GetMapping("/members/{cardId}")
    @Operation(summary = "All members",description = "Get all members by board and work_space")
    public AllMemberResponse getAll(@PathVariable Long cardId){
    return memberService.getAll(cardId);
}

}

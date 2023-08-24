package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;
import peaksoft.house.tasktrackerb9.services.AllIssuesService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/all-issues")
@RequiredArgsConstructor
@Tag(name = "All Issues Api", description = "All Issues endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AllIssuesApi {

    private final AllIssuesService allIssuesService;

    @GetMapping("/filter")
    @Operation(summary = "Filter all Issues", description = "Retrieve a filtered list of issues (cards) within a workspace, using criteria such as date range, labels, and assignees.")
    public List<AllIssuesResponse> filterCards(
            @RequestParam(value = "workSpaceId") Long workSpaceId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(value = "labels", required = false) List<String> labelResponses,
            @RequestParam(value = "assignees", required = false) List<String> assigneeSearchQueries) {
        return allIssuesService.filterIssues(workSpaceId, from, to, labelResponses, assigneeSearchQueries);
    }
}
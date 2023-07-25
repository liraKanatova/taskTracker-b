package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.WorkSpaceRequest;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceResponse;
import peaksoft.house.tasktrackerb9.services.WorkSpaceService;

import java.util.List;


@RestController
@RequestMapping("/api/work_spaces")
@RequiredArgsConstructor
@Tag(name = "Workspace API", description = "All workspace endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WorkSpaceApi {

    private final WorkSpaceService workSpaceService;

    @Operation(summary = "Get all workspaces", description = "Get all workspaces by user auth id")
    @GetMapping
    public List<WorkSpaceResponse> getAllWorkSpaces() {
        return workSpaceService.getAllWorkSpaces();
    }

    @Operation(summary = "Create workSpace", description = "create workspace by user auth id")
    @PostMapping
    public SimpleResponse saveWorkSpaces(@RequestBody WorkSpaceRequest workSpaceRequest) throws MessagingException {
        return workSpaceService.saveWorkSpace(workSpaceRequest);
    }

    @Operation(summary = "Get workspace by id ", description = "get workspace by user auth id and own id")
    @GetMapping("/{workSpaceId}")
    public WorkSpaceResponse getWorkSpaceById(@PathVariable Long workSpaceId) {
        return workSpaceService.getWorkSpaceById(workSpaceId);
    }

    @Operation(summary = "Update workspace by id", description = "get workspace by user auth id and own id")
    @PutMapping("/{workSpaceId}")
    public SimpleResponse updateWorkSpaceById(@PathVariable Long workSpaceId, @RequestBody WorkSpaceRequest workSpaceRequest) {
        return workSpaceService.updateWorkSpaceById(workSpaceId, workSpaceRequest);
    }

    @Operation(summary = "delete workspace by id", description = "get workspace by user auth id and own id")
    @DeleteMapping("/{workSpaceId}")
    public SimpleResponse deleteWorkSpaceById(@PathVariable Long workSpaceId) {
        return workSpaceService.deleteWorkSpaceById(workSpaceId);
    }
}

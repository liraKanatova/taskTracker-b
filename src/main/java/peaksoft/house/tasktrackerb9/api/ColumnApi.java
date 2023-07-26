package peaksoft.house.tasktrackerb9.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.ColumnRequest;
import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.impl.ColumnServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/column")
@RequiredArgsConstructor
@Tag(name = "Column",description = "Api Column to management")
@CrossOrigin(origins = "*",maxAge = 3600)
public class ColumnApi {

    private final ColumnServiceImpl columnService;

    @PostMapping("create-column/{boardId}")
    @Operation(summary = "Create Column",description = "Create column with board id")
    public SimpleResponse createColumn(@PathVariable Long boardId, @RequestBody ColumnRequest columnRequest){
        return columnService.createColumn(boardId, columnRequest);
    }

    @GetMapping("{boardId}")
    @Operation(summary = "Get all columns",description = "Get all columns with board id")
    public List<ColumnResponse> getAll(@PathVariable Long boardId){
        return columnService.getAllColumns(boardId);
    }

    @PutMapping("{columnId}")
    @Operation(summary = "Update column",description = "Update column by columnId")
    public ColumnResponse updateColumn(@PathVariable Long columnId,@RequestBody ColumnRequest columnRequest){
        return columnService.update(columnId, columnRequest);
    }

    @DeleteMapping("{columnId}")
    @Operation(summary = "Remove column",description = "Remove column by columnId")
    public SimpleResponse removeColumn(@PathVariable Long columnId){
        return columnService.removeColumn(columnId);
    }
}

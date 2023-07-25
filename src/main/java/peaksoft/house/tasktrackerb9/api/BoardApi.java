package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.request.BoardUpdateRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boards")
@Tag(name = "Board Api", description = "Api methods for boards")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoardApi {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "Save board", description = "Save board by workspace id")
    public BoardResponse saveBoard(@RequestBody BoardRequest boardRequest) {
        return boardService.saveBoard(boardRequest);
    }

    @GetMapping("/boards/{workSpaceId}")
    @Operation(summary = "Get all boards", description = "Get all boards by workspace id")
    public List<BoardResponse> getAllBoarsByWorkSpace(@PathVariable Long workSpaceId) {
        return boardService.getAllBoardsByWorkspaceId(workSpaceId);
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "Get board", description = "Get board with id")
    public BoardResponse getById(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId);
    }

    @PutMapping
    @Operation(summary = "Update board", description = "Update board with id")
    public SimpleResponse update(@RequestBody BoardUpdateRequest boardUpdateRequest) {
        return boardService.updateBoard(boardUpdateRequest);
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "Delete board", description = "Delete board with id")
    public SimpleResponse deleteBoard(@PathVariable Long boardId) {
        return boardService.deleteBoard(boardId);
    }
}

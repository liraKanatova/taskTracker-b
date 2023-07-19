package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boards")
@Tag(name = "Board Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BoardApi {

    private final BoardService boardService;

    @Operation(summary = "save board", description = "save board with workSpace id")
    @PostMapping("/save/{workSpaceId}")
    public SimpleResponse saveBoard(@RequestBody BoardRequest boardRequest,@PathVariable Long workSpaceId){
        return boardService.saveBoard(boardRequest,workSpaceId);
    }

    @Operation(summary = "all boards", description = "with workSpace id get all boards")
    @GetMapping("/allBoards/{workSpaceId}")
    public List<BoardResponse>getAllBoarsByWorkSpace(@PathVariable Long workSpaceId){
        return boardService.getAllBoardsByWorkspaceId(workSpaceId);
    }

    @Operation(summary = "get board", description = "get board with id")
    @GetMapping("/{boardId}")
    public BoardResponse getById(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId);
    }

    @Operation(summary = "update board", description = "update board with id")
    @PutMapping("/{boardId}")
    public SimpleResponse update(@RequestBody BoardRequest boardRequest,@PathVariable Long boardId) {
        return boardService.updateBoard(boardRequest, boardId);
    }

    @Operation(summary = "delete board", description = "delete board with id")
    @DeleteMapping("/{boardId}")
    public SimpleResponse deleteBoard(@PathVariable Long boardId) {
        return boardService.deleteBoard(boardId);
    }

}

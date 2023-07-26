package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.CommentRequest;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("api/comments")
@Tag(name = "Comments API", description = "All comments endpoints !")
@RequiredArgsConstructor
public class CommentApi {
    private final CommentService commentService;

    @Operation(summary = "Get all", description = "Get all comments by user auth id")
    @GetMapping
    List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @Operation(summary = "Save comment", description = "Save comment by card id and user auth id")
    @PostMapping("/{cardId}")
    SimpleResponse saveComment(@PathVariable Long cardId, @RequestBody CommentRequest commentRequest) {
        return commentService.saveComment(cardId, commentRequest);
    }

    @Operation(summary = "Get by Id", description = "Get comment by user auth id and own id")
    @GetMapping("/{commentId}")
    CommentResponse getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @Operation(summary = "Update comment", description = "Update comment by user auth id and own id")
    @PutMapping("/{commentId}")
    SimpleResponse updateCommentById(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.updateCommentById(commentId, commentRequest);
    }

    @Operation(summary = "Delete comment", description = "Delete comment by user auth id and own id")
    @DeleteMapping("/{commentId}")
    SimpleResponse deleteCommentById(@PathVariable Long commentId) {
        return commentService.deleteCommentById(commentId);
    }
}

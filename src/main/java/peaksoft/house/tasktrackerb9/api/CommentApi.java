package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.CommentRequest;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.CommentService;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("api/comments")
@Tag(name = "Comments API", description = "All comments endpoints !")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentApi {

    private final CommentService commentService;

    @PermitAll
    @Operation(summary = "Get all comments ", description = "Get all comments from workspace")
    @GetMapping("/comments")
    List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @Operation(summary = "Get all comments user", description = "Get all comments by user auth id")
    @GetMapping("user-comments")
    List<CommentResponse> getAllUserComments() {
        return commentService.getAllUserComments();
    }

    @PermitAll
    @Operation(summary = "Get all comments from card", description = "Get all comments from cards by id")
    @GetMapping("/comments/{cardId}")
    List<CommentResponse> getAllCommentsByCardId(@PathVariable Long cardId) {
        return commentService.getAllCommentsFromCard(cardId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Save comment", description = "Save comment by card id and user auth id")
    @PostMapping()
    SimpleResponse saveComment(@RequestBody CommentRequest commentRequest) {
        return commentService.saveComment(commentRequest);
    }

    @PermitAll
    @Operation(summary = "Get by Id", description = "Get comment by user auth id and own id")
    @GetMapping("/{commentId}")
    CommentResponse getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Update comment", description = "Update comment by user auth id and own id")
    @PutMapping("/{commentId}")
    SimpleResponse updateCommentById(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.updateCommentById(commentId, commentRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Delete comment", description = "Delete comment by user auth id and own id")
    @DeleteMapping("/{commentId}")
    SimpleResponse deleteCommentById(@PathVariable Long commentId) {
        return commentService.deleteCommentById(commentId);
    }
}

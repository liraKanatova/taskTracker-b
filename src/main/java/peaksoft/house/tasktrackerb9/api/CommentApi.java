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
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @PermitAll
    @Operation(summary = "Get all comments from card", description = "Get all comments from cards by id")
    @GetMapping("/comments/{cardId}")
    public List<CommentResponse> getAllCommentsByCardId(@PathVariable Long cardId) {
        return commentService.getAllCommentsFromCard(cardId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Save comment", description = "Save comment by card id and user auth id")
    @PostMapping()
    public SimpleResponse saveComment(@RequestBody CommentRequest commentRequest) {
        return commentService.saveComment(commentRequest);
    }

    @PermitAll
    @Operation(summary = "Get comment by id", description = "Get comment by id")
    @GetMapping("/{commentId}")
    CommentResponse getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Update comment", description = "Update comment by user id")
    @PutMapping("/{commentId}")
    public SimpleResponse updateCommentById(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return commentService.updateCommentById(commentId, commentRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Delete comment", description = "Delete comment by user id")
    @DeleteMapping("/{commentId}")
    public SimpleResponse deleteCommentById(@PathVariable Long commentId) {
        return commentService.deleteCommentById(commentId);
    }
}

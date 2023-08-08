package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;

import java.util.List;

public interface CustomCommentRepository {
    List<CommentResponse> getAllUserComments(Long userId);
    List<CommentResponse> getAllCommentByCardId(Long cardId);
    List<CommentResponse> getAllComments();
    CommentResponse getCommentById(Long commentId);

}

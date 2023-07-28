package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;

import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;

import java.util.List;

public interface CommentJdbcTemplateService {
    List<CommentResponse> getAllUserComments(Long userId);
    List<CommentResponse> getAllCommentByCardId(Long userId);
    List<CommentResponse> getAllComments();
    CommentResponse getCommentById(Long commentId);

}

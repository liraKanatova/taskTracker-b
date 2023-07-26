package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;

import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;

import java.util.List;

public interface CommentJdbcTemplateService {
    List<CommentResponse> getAllComments(Long userId);
    CommentResponse getCommentById(Long userId, Long commentId);

}

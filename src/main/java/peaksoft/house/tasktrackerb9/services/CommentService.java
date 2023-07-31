package peaksoft.house.tasktrackerb9.services;


import peaksoft.house.tasktrackerb9.dto.request.CommentRequest;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getAllUserComments();
    List<CommentResponse> getAllComments();
    List<CommentResponse> getAllCommentsFromCard(Long cardId);

    SimpleResponse saveComment(CommentRequest commentRequest);

    CommentResponse getCommentById(Long commentId);

    SimpleResponse updateCommentById(Long commentId, CommentRequest commentRequest);

    SimpleResponse deleteCommentById(Long commentId);

}

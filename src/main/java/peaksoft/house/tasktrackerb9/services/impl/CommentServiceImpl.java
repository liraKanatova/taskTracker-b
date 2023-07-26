package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.CommentRequest;
import peaksoft.house.tasktrackerb9.dto.response.CommentResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Comment;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.CommentRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CommentJdbcTemplateService;
import peaksoft.house.tasktrackerb9.services.CommentService;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentJdbcTemplateService commentJdbcTemplateService;
    private final JwtService jwtService;
    private final CardRepository cardRepository;

    @Override
    public List<CommentResponse> getAllComments() {
        User user = jwtService.getAuthentication();
        return commentJdbcTemplateService.getAllComments(user.getId());
    }
    @Override
    public SimpleResponse saveComment(Long cardId, CommentRequest commentRequest) {
        User user = jwtService.getAuthentication();
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error(String.format("Card with id: %s doesn't exist", cardId) );
            return new NotFoundException(String.format("Card with id: %s doesn't exist", cardId));
        });
        Comment comment = new Comment(commentRequest.comment(), ZonedDateTime.now(), card, user);
        commentRepository.save(comment);
        log.info(String.format("Comment with id: %s successfully saved !", comment.getId()));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Comment with id: %s successfully saved !", comment.getId()))
                .build();
    }
    @Override
    public CommentResponse getCommentById(Long commentId) {
        User user = jwtService.getAuthentication();
        return commentJdbcTemplateService.getCommentById(user.getId(), commentId);
    }
    @Override
    public SimpleResponse updateCommentById(Long commentId, CommentRequest commentRequest) {
        User user = jwtService.getAuthentication();
        Comment comment = commentRepository.getCommentByUserIdAndId(user.getId(), commentId).orElseThrow(() -> {
            log.error(String.format("Card with id: %s or user with id %s doesn't exist", commentId,user.getId()));
            return new NotFoundException(String.format("Card with id: %s or user with id %s doesn't exist", commentId,user.getId()));
        });
        comment.setComment(commentRequest.comment());
        comment.setCreatedDate(ZonedDateTime.now());
        commentRepository.save(comment);
        log.info(String.format("Comment with id: %s successfully updated !", comment.getId()));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Comment with id: %s successfully updated !", comment.getId()))
                .build();
    }

    @Override
    public SimpleResponse deleteCommentById(Long commentId) {
        User user = jwtService.getAuthentication();
        Comment comment = commentRepository.getCommentByUserIdAndId(user.getId(), commentId).orElseThrow(() -> {
            log.error(String.format("Card with id: %s or user with id %s doesn't exist", commentId,user.getId()));
            return new NotFoundException(String.format("Card with id: %s or user with id %s doesn't exist", commentId,user.getId()));
        });
      commentRepository.delete(comment);
      log.info(String.format("Comment with id: %s successfully deleted!", comment.getId()));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Comment with id: %s successfully deleted!", comment.getId()))
                .build();
    }
}

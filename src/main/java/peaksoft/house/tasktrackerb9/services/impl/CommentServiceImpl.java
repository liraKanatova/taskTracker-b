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
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.exceptions.UnauthorizedAccessException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Comment;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.CommentRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomCommentRepository;
import peaksoft.house.tasktrackerb9.services.CommentService;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CustomCommentRepository commentJdbcTemplateService;
    private final JwtService jwtService;
    private final CardRepository cardRepository;

    @Override
    public List<CommentResponse> getAllUserComments() {
        User user = jwtService.getAuthentication();
        return commentJdbcTemplateService.getAllUserComments(user.getId());
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentJdbcTemplateService.getAllComments();
    }

    @Override
    public List<CommentResponse> getAllCommentsFromCard(Long cardId) {
        return commentJdbcTemplateService.getAllCommentByCardId(cardId);
    }

    @Override
    public SimpleResponse saveComment(CommentRequest commentRequest) {
        User user = jwtService.getAuthentication();
        Card card = cardRepository.findById(commentRequest.cardId()).orElseThrow(() -> {
            log.error(String.format("Card with id: %s doesn't exist", commentRequest.cardId()));
            return new NotFoundException(String.format("Card with id: %s doesn't exist", commentRequest.cardId()));
        });
        Comment comment = new Comment(commentRequest.comment(), ZonedDateTime.now(), card,user);
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
        Comment comment = commentRepository.getCommentById(commentId).orElseThrow(() -> {
            log.error(String.format("Comment with id %s doesn't exist!", commentId));
            return new NotFoundException(String.format("Comment with id %s doesn't exist!", commentId));
        });
        if (user.getId().equals(comment.getMember().getId()) || user.getRole() == Role.ADMIN) {
            try {
                return commentJdbcTemplateService.getCommentById(commentId);
            } catch (NotFoundException e) {
                log.error(String.format("Comment with id %s doesn't exist!", commentId));
                throw  new NotFoundException(String.format("Comment with id %s doesn't exist!", commentId));
            } catch (UnauthorizedAccessException e) {
                log.error("Unauthorized to access the comment: " + e.getMessage());
                throw new UnauthorizedAccessException("Unauthorized to access the comment");
            }
        } else {
            log.error("Unauthorized to access the comment");
            throw new UnauthorizedAccessException("Unauthorized to access the comment");
        }
    }

    @Override
    public SimpleResponse updateCommentById(Long commentId, CommentRequest commentRequest) {
        User user = jwtService.getAuthentication();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            log.error(String.format("Comment with id: %s  doesn't exist", commentId));
            return new NotFoundException(String.format("Comment with id: %s doesn't exist", commentId));
        });
        if (user.getRole() == Role.ADMIN || user.getId().equals(comment.getMember().getId())) {
            comment.setComment(commentRequest.comment());
            comment.setCreatedDate(ZonedDateTime.now());
            commentRepository.save(comment);
            log.info(String.format("Comment with id: %s successfully updated !", comment.getId()));
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(String.format("Comment with id: %s successfully updated !", comment.getId()))
                    .build();
        } else {
            log.error("User is not authorized to update the comment");
            throw new UnauthorizedAccessException("User is not authorized to update the comment");
        }

    }

    @Override
    public SimpleResponse deleteCommentById(Long commentId) {
        User user = jwtService.getAuthentication();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            log.error(String.format("Comment with id: %s  doesn't exist", commentId));
            return new NotFoundException(String.format("Comment with id: %s doesn't exist", commentId));
        });
        if (user.getRole() == Role.ADMIN || user.getId().equals(comment.getMember().getId())) {
            commentRepository.delete(comment);
            log.info(String.format("Comment with id: %s successfully deleted !", comment.getId()));
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(String.format("Comment with id: %s successfully  deleted !", comment.getId()))
                    .build();
        } else {
            log.error("User is not authorized to update the comment");
            throw new UnauthorizedAccessException("User is not authorized to update the comment");
        }
    }
}

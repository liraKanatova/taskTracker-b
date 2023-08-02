package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.CardRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateCardRequest;
import peaksoft.house.tasktrackerb9.dto.response.CardInnerPageResponse;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CustomCardJdbcTemplateService;
import peaksoft.house.tasktrackerb9.services.CardService;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final CustomCardJdbcTemplateService jdbcTemplateService;
    private final ItemRepository itemRepository;
    private final AttachmentRepository attachmentRepository;
    private final NotificationRepository notificationRepository;
    private final CheckListRepository checkListRepository;
    private final CommentRepository commentRepository;
    private final EstimationRepository estimationRepository;
    private final UserRepository userRepository;

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User not found!"));
    }

    @Override
    public CardInnerPageResponse getInnerPageCardById(Long cardId) {
        cardRepository.findById(cardId).orElseThrow(() -> {
                    log.error("Card with id: " + cardId + " not found!");
                    return new NotFoundException("Card with id: " + cardId + " not found!");
                }
        );
        return jdbcTemplateService.getAllCardInnerPage(cardId);
    }

    @Override
    public List<CardResponse> getAllCardsByColumnId(Long columnId) {
        columnRepository.findById(columnId).orElseThrow(() -> {
                    log.error("Column with id: " + columnId + " not found!");
                    return new NotFoundException("Column with id: " + columnId + " not found!");
                }
        );
        return jdbcTemplateService.getAllCardsByColumnId(columnId);
    }

    @Override
    public CardInnerPageResponse saveCard(CardRequest cardRequest) {

        User user = getAuthentication();
        Column column = columnRepository.findById(cardRequest.columnId()).orElseThrow(() -> {
                    log.error("Column with id: " + cardRequest.columnId() + " not found!");
                    return new NotFoundException("Column with id: " + cardRequest.columnId() + " not found!");
                }
        );
        Card card = new Card();
        card.setTitle(cardRequest.title());
        card.setDescription(cardRequest.description());
        card.setIsArchive(false);
        card.setCreatedDate(ZonedDateTime.now());
        card.setCreatorId(user.getId());
        user.setCards(List.of(card));
        card.setColumn(column);
        column.setCards(List.of(card));
        cardRepository.save(card);
        log.info("Card with id: " + card.getId() + " successfully saved!");
        return jdbcTemplateService.convertToCardInnerPageResponse(card);
    }


    @Override
    public SimpleResponse updateCardById(UpdateCardRequest updateCardRequest) {

        Card card = cardRepository.findById(updateCardRequest.cardId()).orElseThrow(() -> {
            log.error("Card with id: " + updateCardRequest.cardId() + " not found!");
            return new NotFoundException("Card with id: " + updateCardRequest.cardId() + " not found!");
        });
        card.setTitle(updateCardRequest.title());
        card.setDescription(updateCardRequest.description());
        card.setCreatedDate(ZonedDateTime.now());
        cardRepository.save(card);

        log.info("Card with id: " + updateCardRequest.cardId() + " successfully updated!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " + updateCardRequest.cardId() + " successfully updated!")
                .build();

    }

    @Override
    public SimpleResponse deleteCard(Long id) {

        User user = getAuthentication();
        Card card = cardRepository.findById(id).orElseThrow(() -> {
            log.error("Card with id: " + id + " not found!");
            return new NotFoundException("Card with id: " + id + " not found!");
        });

        if (!user.getId().equals(card.getCreatorId())) {
            log.error("You can't delete this card!");
            throw new BadCredentialException("You can't delete this card!");
        }
        checkListRepository.findAllCheckListByCardId(card.getId()).forEach(checkList -> {
            checkList.getItems().forEach(item -> {
                if (itemRepository.existsById(item.getId())) {
                    itemRepository.deleteById(item.getId());
                } else {
                    log.error("Item with id: " + item.getId() + " not found!");
                    throw new NotFoundException("Item with id: " + item.getId() + " not found!");
                }
            });
            if (checkListRepository.existsById(checkList.getId())) {
                checkListRepository.deleteById(checkList.getId());
            } else {
                log.error("CheckList with id: " + checkList.getId() + " not found!");
                throw new NotFoundException("CheckList with id: " + checkList.getId() + " not found!");
            }
        });
        attachmentRepository.getAllAttachmentsByCardId(card.getId()).forEach(attachment -> {
            if (attachmentRepository.existsById(attachment.getId())) {
                attachmentRepository.deleteById(attachment.getId());
            } else {
                log.error("Attachment with id: " + attachment.getId() + " not found!");
                throw new NotFoundException("Attachment with id: " + attachment.getId() + " not found!");
            }
        });
        commentRepository.findAllCommentsByCardId(card.getId()).forEach(comment -> {
            if (commentRepository.existsById(comment.getId())) {
                commentRepository.deleteById(comment.getId());
            } else {
                log.error("Comment with id: " + comment.getId() + " not found!");
                throw new NotFoundException("Comment with id: " + comment.getId() + " not found!");
            }
        });

        Estimation estimation = estimationRepository.findEstimationByCardId(card.getId());
        if (estimation != null) {
            Notification notification = notificationRepository.findNotificationByEstimationId(estimation.getId());
            if (notification != null) {
                notificationRepository.deleteById(notification.getId());
                Estimation estimation1 = notificationRepository.findEstimationByNotificationId(notification.getId());
                if (estimation1 != null) {
                    estimationRepository.deleteById(estimation1.getId());
                }
            }
            estimationRepository.deleteById(estimation.getId());
        }
        card.getLabels().forEach(label -> label.getCards().remove(card));
        cardRepository.deleteById(card.getId());

        log.info("Card with id: " + id + " successfully deleted!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " + id + " successfully deleted!")
                .build();
    }

}
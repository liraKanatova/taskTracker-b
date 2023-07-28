package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.CardRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateCardRequest;
import peaksoft.house.tasktrackerb9.dto.response.CardInnerPageResponse;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Column;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CustomCardJdbcTemplateService;
import peaksoft.house.tasktrackerb9.services.CardService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final CustomCardJdbcTemplateService jdbcTemplateService;
    private final EntityManager entityManager;
    private final CommentRepository commentRepository;
    private final CheckListRepository checkListRepository;
    private final EstimationRepository estimationRepository;
    private final UserRepository  userRepository;
    private final LabelRepository labelRepository;


    @Override
    public CardInnerPageResponse getInnerPageCardById(Long cardId){
        return jdbcTemplateService.getAllCardInnerPage(cardId);
    }

    @Override
    public List<CardResponse> getAllCardsByColumnId(Long columnId) {
        columnRepository.findById(columnId).orElseThrow(
                () -> {
                    log.error("Column with id: " + columnId + " not found!");
                    return new NotFoundException("Column with id: " + columnId+ " not found!");
                }
        );
        return jdbcTemplateService.getAllCardsByColumnId(columnId);
    }


//    private List<CheckList> convertCheckListRequestsToCheckLists(List<CheckListRequest> checkListRequests) {
//        List<CheckList> checkLists = new ArrayList<>();
//        for (CheckListRequest checkListRequest : checkListRequests) {
//            CheckList checkList = new CheckList();
//            checkList.setDescription(checkListRequest.description());
//            checkLists.add(checkList);
//            checkListRepository.save(checkList);
//        }
//        return checkLists;
//    }
//    private List<Comment> convertCommentListToCommentList(List<CommentRequest> commentRequests){
//        List<Comment> comments = new ArrayList<>();
//        for(CommentRequest commentRequest : commentRequests){
//            Comment comment = new Comment();
//            comment.setCreatedDate(ZonedDateTime.now());
//            comment.setComment(commentRequest.comment());
//            comments.add(comment);
//            commentRepository.save(comment);
//        }
//        return comments;
//
//    }
//
//    private Estimation convertEstimationRequestToEstimation(EstimationRequest estimationRequest) {
//        Estimation estimation = new Estimation();
//        estimation.setStartDate(estimationRequest.startDate());
//        estimation.setDuetDate(estimationRequest.duetDate());
//        estimation.setTime(estimationRequest.time());
//        estimation.setReminderType(estimationRequest.reminderType());
//        estimationRepository.save(estimation);
//        return estimation;
//    }
//
//    private List<Label> convertLabelRequestsToLabels(List<LabelRequest> labelRequests) {
//        List<Label> labels = new ArrayList<>();
//        for (LabelRequest labelRequest : labelRequests) {
//            Label label = new Label();
//            label.setLabelName(labelRequest.labelName());
//            label.setColor(labelRequest.color());
//            labels.add(label);
//            labelRepository.save(label);
//        }
//        return labels;
//    }
//
//    private List<User> convertUserRequestToUser(List<Long> usersIds){
//        List<User> users = new ArrayList<>();
//        for(Long userRequest : usersIds){
//            User user = new User();
//            usersIds.add(userRequest);
//            users.add(user);
//            userRepository.save(user);
//        }
//        return users;
//    }

    @Override
    public CardInnerPageResponse saveCard(CardRequest cardRequest) {

        Column column = columnRepository.findById(cardRequest.columnId()).orElseThrow(
                () -> {
                    log.error("Column with id: " + cardRequest.columnId() + " not found!");
                    return new NotFoundException("Column with id: " + cardRequest.columnId() + " not found!");
                }
        );
//        Card card = new Card();
//        card.setTitle(cardRequest.title());
//        card.setDescription(cardRequest.description());
//        card.setIsArchive(false);
//        card.setCreatedDate(ZonedDateTime.now());
//
//
//        List<CheckList> checkLists = convertCheckListRequestsToCheckLists(cardRequest.checkListRequests());
//        card.setCheckLists(checkLists);
//
//        Estimation estimation = convertEstimationRequestToEstimation(cardRequest.estimationRequest());
//        card.setEstimation(estimation);
//
//        List<Label> labels = convertLabelRequestsToLabels(cardRequest.labelRequests());
//        card.setLabels(labels);
//
//        List<Comment> comments = convertCommentListToCommentList(cardRequest.commentRequests());
//        card.setComments(comments);
//
//        List<User> users = convertUserRequestToUser(cardRequest.usersIds());
//        card.setUsers(users);
//
//        card.setColumn(column);
//        column.setCards(List.of(card));
//        cardRepository.save(card);

        CardInnerPageResponse cardInnerPageResponse = new CardInnerPageResponse();

        return cardInnerPageResponse;
    }





    @Override
    public SimpleResponse createCard(CardRequest cardRequest) {

        Column column2 = columnRepository.findById(cardRequest.columnId()).orElseThrow(
                () -> {
                    log.error("Column with id: " + cardRequest.columnId() + " not found!");
                    return new NotFoundException("Column with id: " + cardRequest.columnId() + " not found!");
                }
        );
        Card card = new Card();
        card.setTitle(cardRequest.title());
        card.setDescription(cardRequest.description());
        card.setIsArchive(false);
        card.setCreatedDate(ZonedDateTime.now());
        card.setColumn(column2);
        column2.setCards(List.of(card));
        cardRepository.save(card);

        log.info("Card with id: " + card.getId() + " successfully saved!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " + card.getId() + " successfully saved!")
                .build();
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

            log.info("Card with id: " +updateCardRequest.cardId() + " successfully updated!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " +updateCardRequest.cardId() + " successfully updated!")
                .build();

    }

    @Override
    public SimpleResponse deleteCard(Long id) {

        Card card = cardRepository.findById(id).orElseThrow(() -> {
            log.error("Card with id: " + id + " not found!");
            return new NotFoundException("Card with id: " + id + " not found!");
        });
            cardRepository.delete(card);
            log.info("Card with id: " + id + " deleted!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " + id + " deleted!")
                .build();
    }
}
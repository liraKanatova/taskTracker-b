package peaksoft.house.tasktrackerb9.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import peaksoft.house.tasktrackerb9.dto.response.*;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardConverter {

    private final CardRepository cardRepository;
    private final EstimationRepository estimationRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final CheckListRepository checklistRepository;

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User not found!"));
    }

    public CardInnerPageResponse convertToCardInnerPageResponse(Card card) {
        CardInnerPageResponse cardInnerPageResponse = new CardInnerPageResponse();
        cardInnerPageResponse.setCardId(card.getId());
        cardInnerPageResponse.setTitle(card.getTitle());
        cardInnerPageResponse.setDescription(card.getDescription());
        cardInnerPageResponse.setIsArchive(card.getIsArchive());
        List<LabelResponse> list = new ArrayList<>();
        if (card.getLabels() != null) {
            for (LabelResponse  l : labelRepository.getAllLabelResponse()) {
                LabelResponse labelResponse = new LabelResponse();
                labelResponse.setLabelId(l.getLabelId());
                labelResponse.setDescription(l.getDescription());
                labelResponse.setColor(l.getColor());
                list.add(labelResponse);
            }
            cardInnerPageResponse.setLabelResponses(list);
        }else {
            cardInnerPageResponse.setLabelResponses(new ArrayList<>());
        }

        if (card.getEstimation() != null) {
            cardInnerPageResponse.setEstimationResponse(getEstimationByCardIdd(card.getId()));
        }else {
            cardInnerPageResponse.setEstimationResponse(new EstimationResponse());
        }

        if (card.getMembers() != null) {
            cardInnerPageResponse.setUserResponses(getAllCardMembers(card.getMembers()));
        }else{
            cardInnerPageResponse.setUserResponses(new ArrayList<>());
        }
        cardInnerPageResponse.setChecklistResponses(getCheckListResponses(checklistRepository.findAllCheckListByCardId(card.getId())));
        if (card.getComments() != null) {
            cardInnerPageResponse.setCommentResponses(getCommentsResponse(card.getComments()));
        }else {
            cardInnerPageResponse.setCommentResponses(new ArrayList<>());
        }

        return cardInnerPageResponse;
    }

    private CommentResponse convertCommentToResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentId(comment.getId());
        commentResponse.setComment(comment.getComment());
        commentResponse.setCreatedDate(comment.getCreatedDate().toString());
        commentResponse.setCreatorId(getAuthentication().getId());
        commentResponse.setCreatorName(getAuthentication().getFirstName());
        commentResponse.setCreatorAvatar(getAuthentication().getImage());
        return commentResponse;

    }

    private List<CommentResponse> getCommentsResponse(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }
        return comments.stream().map(this::convertCommentToResponse).collect(Collectors.toList());
    }

    private List<CheckListResponse> getCheckListResponses(List<CheckList> checklists) {
        if (checklists == null) {
            return Collections.emptyList();
        }
        return checklists.stream().map(this::convertCheckListToResponse).collect(Collectors.toList());
    }

    public CheckListResponse convertCheckListToResponse(CheckList checklist) {
        CheckListResponse checkListResponse = new CheckListResponse();
        checkListResponse.setCheckListId(checklist.getId());
        checkListResponse.setDescription(checklist.getDescription());

        List<Item> items = checklist.getItems();
        List<ItemResponse> itemResponses = new ArrayList<>();
        int countOfItems = items.size();
        int countOfCompletedItems = 0;

        for (Item item : items) {
            ItemResponse itemResponse = new ItemResponse();
            itemResponse.setItemId(item.getId());
            itemResponse.setTitle(item.getTitle());
            itemResponse.setIsDone(item.getIsDone());
            itemResponses.add(itemResponse);

            if (item.getIsDone().equals(true)) {
                countOfCompletedItems++;
            }
        }
        int count = (countOfItems > 0) ? (countOfCompletedItems * 100) / countOfItems : 0;
        String counter = countOfCompletedItems + "/" + countOfItems;

        checkListResponse.setPercent(count);
        checkListResponse.setCounter(counter);
        checkListResponse.setItemResponseList(itemResponses);

        checklist.setPercent(count);
        checklistRepository.save(checklist);

        return checkListResponse;
    }

    private EstimationResponse getEstimationByCardIdd(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found!");
            return new NotFoundException("Card with id: " + cardId + " not found!");
        });
        Estimation estimation = estimationRepository.findById(card.getEstimation().getId()).orElseThrow(() -> {
            log.error("Estimation with id: " + card.getEstimation().getId()  + " not found!");
            return new NotFoundException("Estimation with id: " + card.getEstimation().getId() + " not found!");
        });
        EstimationResponse estimationResponse = new EstimationResponse();
        estimationResponse.setEstimationId(estimation.getId());
        estimationResponse.setStartDate(estimation.getStartDate().toString());
        estimationResponse.setDuetDate(estimation.getDuetDate().toString());
        estimationResponse.setFinishTime(estimation.getTime().toString());
        estimationResponse.setReminderType(estimation.getReminderType());
        return estimationResponse;

    }

    private List<UserResponse> getAllCardMembers(List<User> users) {
        return users.stream().map(this::convertToUserResponse).collect(Collectors.toList());
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setAvatar(user.getImage());
        return userResponse;
    }
}
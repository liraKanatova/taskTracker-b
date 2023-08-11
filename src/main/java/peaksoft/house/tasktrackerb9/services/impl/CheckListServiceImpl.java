package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.converter.CardConverter;
import peaksoft.house.tasktrackerb9.dto.request.CheckListRequest;
import peaksoft.house.tasktrackerb9.dto.response.CheckListResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomCheckListRepository;
import peaksoft.house.tasktrackerb9.services.CheckListService;

import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CheckListServiceImpl implements CheckListService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CheckListRepository checkListRepository;
    private final CardConverter converter;
    private final CustomCheckListRepository customCheckListRepository;

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User not found!"));
    }

    @Override
    public CheckListResponse saveCheckList(Long cardId, CheckListRequest checkListRequest) {

        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found!");
            return new NotFoundException("Card with id: " + cardId + " not found!");
        });

        CheckList checkList = new CheckList();
        checkList.setDescription(checkListRequest.description());
        checkList.setCard(card);
        log.info("CheckList successfully saved!");
        return converter.convertCheckListToResponse(checkListRepository.save(checkList));
    }

    @Override
    public List<CheckListResponse> getAllCheckListByCardId(Long cardId) {
        cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found!");
            return new NotFoundException("Card with id: " + cardId + " not found!");
        });
        return customCheckListRepository.getAllCheckListByCardId(cardId);
    }

    @Override
    public CheckListResponse updateCheckListById(Long checkListId, CheckListRequest checkListRequest) {

        CheckList checkList = checkListRepository.findById(checkListId).orElseThrow(() -> {
            log.error("CheckList with id: " + checkListId + " not found!");
            return new NotFoundException("CheckList with id: " + checkListId + " not found!");
        });

        checkList.setDescription(checkListRequest.description());
        log.info("CheckList successfully updated!");
        return converter.convertCheckListToResponse(checkListRepository.save(checkList));
    }

    @Override
    public SimpleResponse deleteCheckList(Long checkListId) {

        CheckList checkList = checkListRepository.findById(checkListId).orElseThrow(() -> {
            log.error("CheckList with id: " + checkListId + " not found!");
            return new NotFoundException("CheckList with id: " + checkListId + " not found!");
        });

        List<Item> items = checkList.getItems();
        items.forEach(item -> item.getCheckList().getItems().remove(checkList));

        checkListRepository.delete(checkList);

               log.info("CheckList with id: " + checkListId + " successfully deleted!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("CheckList with id: " + checkListId + " successfully deleted!")
                .build();
    }
}

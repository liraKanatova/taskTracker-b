package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.converter.CardConverter;
import peaksoft.house.tasktrackerb9.dto.request.CardRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateCardRequest;
import peaksoft.house.tasktrackerb9.dto.response.CardInnerPageResponse;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.NotificationType;
import peaksoft.house.tasktrackerb9.enums.Role;
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
    private final UserRepository userRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final CardConverter cardConverter;
    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;
    private final JwtService jwtService;

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User not found!"));
    }

    @Override
    public SimpleResponse archiveCard(Long cardId) {

        User user = getAuthentication();
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found!");
            return new NotFoundException("Card with id: " + cardId + " not found!");
        });

        Column column = columnRepository.findById(card.getColumn().getId()).orElseThrow(() -> {
            log.error("Column with id: " + card.getColumn().getId() + " not found!");
            return new NotFoundException("Column with id: " + card.getColumn().getId() + " not found!");
        });

        WorkSpace workSpace = workSpaceRepository.findById(column.getBoard().getWorkSpace().getId()).orElseThrow(() -> {
            log.error("WorkSpace with id: " + column.getBoard().getWorkSpace().getId() + " not found!");
            return new NotFoundException("WorkSpace with id: " + column.getBoard().getWorkSpace().getId() + " not found!");
        });

        UserWorkSpaceRole userWorkSpaceRole = userWorkSpaceRoleRepository.findByUserIdAndWorkSpacesId(user.getId(), workSpace.getId());
        if (userWorkSpaceRole == null) {
            log.error("You are not a member of this workspace!");
            throw new BadCredentialException("You are not a member of this workspace!");
        }

        if (workSpace.getMembers().contains(userWorkSpaceRole.getMember())) {
            card.setIsArchive(!card.getIsArchive());
            cardRepository.save(card);

            String message = card.getIsArchive() ? "Card with id: " + cardId + " successfully archived!" : "Card with id: " + cardId + " successfully unArchived!";
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(message)
                    .build();
        } else {
            log.error("You can't archive this card!");
            throw new BadCredentialException("You can't archive this card!");
        }
    }

    @Override
    public SimpleResponse deleteAllCardsInColumn(Long columnId) {

        User user = getAuthentication();
        Column column = columnRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column with id: " + columnId + " not found!");
            return new NotFoundException("Column with id: " + columnId + " not found!");
        });

        UserWorkSpaceRole userRole = userWorkSpaceRoleRepository.findByUserIdAndWorkSpacesId(user.getId(), column.getBoard().getWorkSpace().getId());
        if (userRole == null || !userRole.getRole().equals(Role.ADMIN)) {
            log.error("You don't have permission to delete cards in this column");
            throw new BadCredentialException("You don't have permission to delete cards in this column");
        }

        if (!column.getCards().isEmpty()) {
            cardRepository.deleteAll(column.getCards());
        } else {
            log.error("No cards have this colum!");
            throw new NotFoundException("No cards have this colum!");
        }
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("All cards from this column with id: " + columnId + " are removed!")
                .build();
    }

    @Override
    public SimpleResponse archiveAllCardsInColumn(Long columnId) {

        User user = getAuthentication();
        Column column = columnRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column with id: " + columnId + " not found!");
            return new NotFoundException("Column with id: " + columnId + " not found!");
        });

        UserWorkSpaceRole userRole = userWorkSpaceRoleRepository.findByUserIdAndWorkSpacesId(user.getId(), column.getBoard().getWorkSpace().getId());
        if (userRole == null || !userRole.getRole().equals(Role.ADMIN)) {
            log.error("You don't have permission to archive cards in this column");
            throw new BadCredentialException("You don't have permission to archive cards in this column");
        }

        if (!column.getCards().isEmpty()) {
            for (Card c : column.getCards()) {
                c.setIsArchive(true);
            }
        } else {
            log.error("No cards have this colum!");
            throw new NotFoundException("No cards have this colum!");
        }
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("All cards from this column with id: " + columnId + " are archived!")
                .build();
    }

    @Override
    public SimpleResponse moveCard(Long cardId, Long columnId) {

        User user = jwtService.getAuthentication();
        Card movedCard = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found!");
            return new NotFoundException("Card with id: " + cardId + " not found!");
        });

        Column targetColumn = columnRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column with id: " + columnId + " not found!");
            return new NotFoundException("Column with id: " + columnId + " not found!");
        });

        Notification moveNotification = new Notification();
        moveNotification.setType(NotificationType.MOVE);
        moveNotification.setText(String.format("Card with id %d has been moved to column with id %d", cardId, columnId));
        moveNotification.setCreatedDate(ZonedDateTime.now());
        moveNotification.setIsRead(false);
        moveNotification.setColumnId(targetColumn.getId());
        moveNotification.setBoardId(targetColumn.getBoard().getId());
        moveNotification.setFromUserId(user.getId());
        moveNotification.setCard(movedCard);

        movedCard.getNotifications().add(moveNotification);
        for (User member : movedCard.getMembers()) {
            member.getNotifications().add(moveNotification);
        }
        cardRepository.save(movedCard);
        columnRepository.save(targetColumn);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " + cardId + " has been moved to column with id: " + columnId)
                .build();
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
        return cardConverter.convertToCardInnerPageResponse(card);
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

        card.getLabels().forEach(label -> label.getCards().remove(card));
        cardRepository.deleteById(card.getId());

        log.info("Card with id: " + id + " successfully deleted!");
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Card with id: " + id + " successfully deleted!")
                .build();
    }

}
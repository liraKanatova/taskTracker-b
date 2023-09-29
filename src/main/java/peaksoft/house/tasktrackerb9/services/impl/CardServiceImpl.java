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
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.CustomCardJdbcTemplateService;
import peaksoft.house.tasktrackerb9.services.CardService;

import java.time.ZoneId;
import java.time.LocalDate;
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
    private final CardConverter cardConverter;
    private final JwtService jwtService;
    private final NotificationRepository notificationRepository;

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("User not found!"));
    }

    @Override
    public SimpleResponse archiveCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found!");
            return new NotFoundException("Card with id: " + cardId + " not found!");
        });
            card.setIsArchive(!card.getIsArchive());
            cardRepository.save(card);

            String message = card.getIsArchive() ? "Card with id: " + cardId + " successfully archived!" : "Card with id: " + cardId + " successfully unArchived!";
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(message)
                    .build();

    }

    @Override
    public SimpleResponse deleteAllCardsInColumn(Long columnId) {
        List<Card> cards = cardRepository.getCardsByColumnId(columnId);
        cardRepository.deleteAll(cards);
        return SimpleResponse.builder()
                .message("successfully removed inside the columa")
                .status(HttpStatus.OK)
                .build();

    }

    @Override
    public SimpleResponse archiveAllCardsInColumn(Long columnId) {
        Column column = columnRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column with id: " + columnId + " not found!");
            return new NotFoundException("Column with id: " + columnId + " not found!");
        });

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

        movedCard.getColumn().getCards().remove(movedCard);
        movedCard.setColumn(targetColumn);
        targetColumn.getCards().add(movedCard);

        Notification moveNotification = new Notification();
        moveNotification.setType(NotificationType.MOVE);
        moveNotification.setText("Card with id " + cardId + " has been moved to column " + targetColumn.getTitle());
        moveNotification.setCreatedDate(ZonedDateTime.now(ZoneId.of("Asia/Bishkek")));
        moveNotification.setIsRead(false);
        moveNotification.setColumnId(targetColumn.getId());
        moveNotification.setBoardId(targetColumn.getBoard().getId());
        moveNotification.setFromUserId(user.getId());
        moveNotification.setCard(movedCard);

        movedCard.addNotification(moveNotification);
        for (User member : movedCard.getMembers()) {
            member.getNotifications().add(moveNotification);
        }
        notificationRepository.save(moveNotification);
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
        card.setCreatedDate(LocalDate.now());
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
        card.setCreatedDate(LocalDate.now());
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
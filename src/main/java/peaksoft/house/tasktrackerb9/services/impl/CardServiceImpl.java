package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.services.CardService;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ColumnsRepository columnRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;
    private final UserRepository userRepository;

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

        WorkSpace workSpace= workSpaceRepository.findById(column.getBoard().getWorkSpace().getId()).orElseThrow(() -> {
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
                .message("All cards from this column with id: " + columnId +" are removed!")
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
                .message("All cards from this column with id: " + columnId +" are archived!")
                .build();
    }

}
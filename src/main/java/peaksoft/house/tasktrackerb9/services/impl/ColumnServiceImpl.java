package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.ColumnRequest;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.*;
import peaksoft.house.tasktrackerb9.repositories.*;
import peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl.CustomColumnRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.ColumnService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ColumnServiceImpl implements ColumnService {

    private final ColumnsRepository columnsRepository;

    private final BoardRepository boardRepository;

    private final CustomColumnRepositoryImpl columns;

    private final JwtService jwtService;

    private final WorkSpaceRepository workSpaceRepository;

    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    @Override
    public SimpleResponse createColumn(ColumnRequest columnRequest) {
        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(columnRequest.boardId()).orElseThrow(() -> {
            log.error("Column with id: " + columnRequest.boardId() + " not found");
            return new NotFoundException("Column with id: " + columnRequest.boardId() + " not found");
        });
        Column column = new Column();
        if (user.getRole().equals(Role.ADMIN)) {
            column.setTitle(columnRequest.title());
            column.setIsArchive(false);
            board.getColumns().add(column);
            column.setBoard(board);
            user.getColumns().add(column);
            column.setMembers(List.of(user));
            columnsRepository.save(column);
            log.info("Successfully created");
            return SimpleResponse.builder()
                    .message("Successfully create column")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            throw new BadCredentialException("You are not member");
        }
    }

    @Override
    public List<ColumnResponse> getAllColumns(Long boardId) {
        return columns.getAllColumns(boardId);
    }

    @Override
    public ColumnResponse update(Long columnId, ColumnRequest columnRequest) {
        User user = jwtService.getAuthentication();
        Column column = columnsRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column not found!");
            return new NotFoundException("Column with id: " + columnId + " not found");
        });
        if (user.getRole().equals(Role.ADMIN)) {
            column.setTitle(columnRequest.title());
            columnsRepository.save(column);
            log.info("Column successfully update");
        } else {
            throw new BadCredentialException("You are not member");
        }
        return new ColumnResponse(column.getId(), column.getTitle(), column.getIsArchive());
    }

    @Override
    public SimpleResponse removeColumn(Long columnId) {
        User user = jwtService.getAuthentication();
        Column column = columnsRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column not found!");
            return new NotFoundException("Column with id: " + columnId + " not found");
        });
        if (user.getRole().equals(Role.ADMIN)) {
            columnsRepository.delete(column);
            log.info("Column  successfully deleted");
        } else throw new BadCredentialException("You are not member");
        return SimpleResponse.builder()
                .message("Column successfully deleted")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public SimpleResponse sendToArchive(Long columnId) {

        User user = jwtService.getAuthentication();


        Column column = columnsRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column with id: " + columnId + " not found!");
            return new NotFoundException("Column with id: " + columnId + " not found!");
        });

        WorkSpace workSpace = workSpaceRepository.findById(column.getBoard().getWorkSpace().getId()).orElseThrow(() -> {
            log.error("WorkSpace with id: " + column.getBoard().getWorkSpace().getId() + " not found!");
            return new NotFoundException("WorkSpace with id: " + column.getBoard().getWorkSpace().getId() + " not found!");
        });

        User workspaceAdmin = userRepository.findById(workSpace.getAdminId()).orElseThrow(
                () -> new NotFoundException("Workspace admin with id: " + workSpace.getAdminId() + " not found!")
        );

        UserWorkSpaceRole userWorkSpaceRole = userWorkSpaceRoleRepository.findByUserIdAndWorkSpacesId(user.getId(), workSpace.getId());
        if (userWorkSpaceRole == null) {
            log.error("You are not a member of this workspace!");
            throw new BadCredentialException("You are not a member of this workspace!" + workSpace.getName() + "/" + user.getFirstName());
        }

        if (workSpace.getMembers().contains(userWorkSpaceRole.getMember()) || userWorkSpaceRole.getMember().equals(workspaceAdmin)) {
            column.setIsArchive(!column.getIsArchive());
            columnsRepository.save(column);

            List<Card> cardsInColumn = new ArrayList<>(column.getCards());
            for (Card card : cardsInColumn) {
                card.setIsArchive(column.getIsArchive());
                cardRepository.save(card);
            }

            String message = column.getIsArchive() ? "Column with id: " + columnId + " successfully archived!" : "Column with id: " + columnId + " successfully unArchived!";
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
    public List<CardResponse> getAllCardsByColumnId(Long columnId) {
        User admin = jwtService.getAuthentication();
        Column column = columnsRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column not found!");
            return new NotFoundException("Column with id: " + columnId + " not found");
        });
        WorkSpace workSpace = column.getBoard().getWorkSpace();
        if (!workSpace.getAdminId().equals(admin.getId())) {
            log.error("User with id : {} is not an admin of this workSpace", admin.getId());
            throw new BadCredentialException("You are not a admin of this workSpace");
        }
        List<Card> cardsInColumn = new ArrayList<>(column.getCards());
        return cardsInColumn.stream()
                .map(card -> new CardResponse(card.getId(), card.getTitle()))
                .toList();
    }
}
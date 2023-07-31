package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.ColumnRequest;
import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Board;
import peaksoft.house.tasktrackerb9.models.Column;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.repositories.BoardRepository;
import peaksoft.house.tasktrackerb9.repositories.ColumnsRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl.ColumnsJdbcTemplateServiceImpl;
import peaksoft.house.tasktrackerb9.services.ColumnService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ColumnServiceImpl implements ColumnService {

    private final ColumnsRepository columnsRepository;

    private final BoardRepository boardRepository;

    private final ColumnsJdbcTemplateServiceImpl columns;

    private final JwtService jwtService;

    @Override
    public SimpleResponse createColumn(ColumnRequest columnRequest) {
        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(columnRequest.boardId()).orElseThrow(() -> {
            log.error("Column with id: " + columnRequest.boardId() + " not found");
            return new NotFoundException("Column with id: " + columnRequest.boardId() + " not found");
        });
        Column column = new Column();
        if (user != null) {
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
            return new NotFoundException("Column with id: "+columnId+" not found");
        });
        if (user != null) {
            column.setTitle(columnRequest.title());
            columnsRepository.save(column);
             log.info("Column successfully updated");
        } else {
            throw new BadCredentialException("You are not member");
        }
        return new ColumnResponse(column.getId(), column.getTitle());
    }

    @Override
    public SimpleResponse removeColumn(Long columnId) {
        User user = jwtService.getAuthentication();
        Column column = columnsRepository.findById(columnId).orElseThrow(() -> {
            log.error("Column not found!");
            return new NotFoundException("Column with id: "+columnId+" not found");
        });
        if (user != null) {
            columnsRepository.delete(column);
            log.info("Column  successfully deleted");
        } else throw new BadCredentialException("You are not member");
        return SimpleResponse.builder()
                .message("Column successfully deleted")
                .status(HttpStatus.OK)
                .build();
    }
}

package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Board;
import peaksoft.house.tasktrackerb9.models.Favorite;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.BoardRepository;
import peaksoft.house.tasktrackerb9.services.BoardService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final WorkSpaceRepository workspaceRepository;

    private final JwtService jwtService;

    @Override
    public List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId) {
        List<BoardResponse> boardsList = new ArrayList<>();
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workspaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: "+workSpaceId+" not found");
                    throw new NotFoundException("WorkSpace with id: "+workSpaceId+" not found");
                });
        if(workSpace.getBoards() ==null){
            log.error("This is board not found in this workSpace");
            throw new NotFoundException("This is board not found in this workSpace");
        }
            for (Board board : workSpace.getBoards()) {
                boolean isFavorite = false;
                if (board.getFavorite() != null) {
                    for (Favorite favorite : user.getFavorites()) {
                        if (board.getFavorite().equals(favorite)) {
                            isFavorite = true;
                            break;
                        }
                    }
                }
                BoardResponse boardResponse = new BoardResponse(
                        board.getId(),
                        board.getTitle(),
                        board.getBackGround(),
                        isFavorite
                );
                boardsList.add(boardResponse);
            }
            return boardsList;
    }

    @Override
    public SimpleResponse saveBoard(BoardRequest boardRequest, Long workSpaceId) {
        WorkSpace workSpace = workspaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: "+workSpaceId+" not found");
                   throw  new NotFoundException("WorkSpace with id: "+workSpaceId+" not found");
                });
            Board board = new Board();
            board.setTitle(boardRequest.title());
            board.setBackGround(boardRequest.backGround());
            board.setWorkSpace(workSpace);
            workSpace.getBoards().add(board);
            boardRepository.save(board);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Board successfully saved")
                    .build();
    }

    @Override
    public SimpleResponse updateBoard(BoardRequest boardRequest, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("Board with id: " + boardId + " not found");
                            throw new NotFoundException("Board with id: " + boardId + " not found");
                        });
            board.setTitle(boardRequest.title());
            board.setBackGround(boardRequest.backGround());
            boardRepository.save(board);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Board updated successfully ")
                    .build();
        }


    @Override
    public SimpleResponse deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("Board with id: " + boardId + " not found");
                    throw new NotFoundException("Board with id: " + boardId + " not found");
                });
        WorkSpace workSpace = board.getWorkSpace();
            workSpace.getBoards().remove(board);
            boardRepository.deleteById(boardId);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Board successfully deleted")
                .build();
    }

    @Override
    public BoardResponse getBoardById(Long boardId) {
        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("Board with id: " + boardId + " not found");
                   throw  new NotFoundException("Board with id: " + boardId + " not found");
                });
        boolean isFavorite = false;
        if (board.getFavorite() != null) {
            for (Favorite favorite : user.getFavorites()) {
                if(board.getFavorite().equals(favorite)){
                    isFavorite =true;
                    break;
                }
            }
        }
        return BoardResponse.builder()
                .boardId(boardId)
                .title(board.getTitle())
                .backGround(board.getBackGround())
                .isFavorite(isFavorite)
                .build();
    }
}

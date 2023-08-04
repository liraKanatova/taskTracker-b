package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.request.BoardUpdateRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.BadRequestException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Board;
import peaksoft.house.tasktrackerb9.models.Favorite;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.BoardRepository;
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl.CustomBoardRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.BoardService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final CustomBoardRepositoryImpl customBoardRepository;

    private final WorkSpaceRepository workspaceRepository;

    private final JwtService jwtService;

    @Override
    public List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId) {
        WorkSpace workSpace = workspaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + workSpaceId + " not found");
                    throw new NotFoundException("WorkSpace with id: " + workSpaceId + " not found");
                });
        return customBoardRepository.getAllBoardsByWorkspaceId(workSpace.getId());
    }

    @Override
    public BoardResponse saveBoard(BoardRequest boardRequest) {

        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workspaceRepository.findById(boardRequest.workSpaceId())
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + boardRequest.workSpaceId() + " not found");
                    return new NotFoundException("WorkSpace with id: " + boardRequest.workSpaceId() + " not found");
                });
        Board board = new Board();
        board.setTitle(boardRequest.title());
        board.setBackGround(boardRequest.backGround());
        board.setWorkSpace(workSpace);
        workSpace.getBoards().add(board);
        board.setMembers(List.of(user));
        boardRepository.save(board);
        boolean isFavorite = false;
        if(board.getFavorites() !=null){
            for (Favorite favorite : user.getFavorites()) {
                if(board.getFavorites().contains(favorite)){
                    isFavorite = true;
                    break;
                }
            }
        }
        return BoardResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .backGround(board.getBackGround())
                .isFavorite(isFavorite)
                .build();
    }

    @Override
    public SimpleResponse updateBoard(BoardUpdateRequest boardUpdateRequest) {

        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(boardUpdateRequest.boardI())
                .orElseThrow(() -> {
                    log.error("Board with id: " + boardUpdateRequest.boardI() + " not found");
                    throw new NotFoundException("Board with id: " + boardUpdateRequest.boardI() + " not found");
                });
        if(!user.getBoards().contains(board)){
            throw new BadRequestException("Board not found");
        }
        board.setTitle(boardUpdateRequest.title());
        board.setBackGround(boardUpdateRequest.backGround());
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
                    return new NotFoundException("Board with id: " + boardId + " not found");
                });
        WorkSpace workSpace = board.getWorkSpace();
        if(workSpace != null) {
            workSpace.getBoards().remove(board);
        }
        List<Favorite> favorites = board.getFavorites();
        for (Favorite favorite : favorites) {
            favorite.setBoard(null);
        }
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
                    throw new NotFoundException("Board with id: " + boardId + " not found");
                });
        boolean isFavorite = false;
        if (board.getFavorites() != null) {
            for (Favorite favorite : user.getFavorites()) {
                if (board.getFavorites().contains(favorite)) {
                    isFavorite = true;
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

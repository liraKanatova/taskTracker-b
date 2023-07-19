package peaksoft.house.tasktrackerb9.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.BoardRequest;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.entity.Board;
import peaksoft.house.tasktrackerb9.entity.Favorite;
import peaksoft.house.tasktrackerb9.entity.User;
import peaksoft.house.tasktrackerb9.entity.WorkSpace;
import peaksoft.house.tasktrackerb9.repository.BoardRepository;
import peaksoft.house.tasktrackerb9.repository.UserWorkSpaceRoleRepository;
import peaksoft.house.tasktrackerb9.service.BoardService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final WorkSpaceRepository workspaceRepository;

    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;

    private final JwtService jwtService;

    @Override
    public List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId) {
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workspaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace id not found!");
                    throw new NotFoundException("WorkSpace id not found!");
                });
        if (workSpace.getRoles().contains(userWorkSpaceRoleRepository.getUserIdAndWorkSpaceId(user.getId(), workSpaceId))) {
            List<Board> list = boardRepository.getAllByBoards(workSpaceId);
            List<BoardResponse> boardsList = new ArrayList<>();
            for (Board board : list) {
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
        } else throw new BadRequestException("In this workSpace not found user");
    }

    @Override
    public SimpleResponse saveBoard(BoardRequest boardRequest, Long workSpaceId) {
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workspaceRepository.findById(boardRequest.getWorkspaceId())
                .orElseThrow(() -> {
                    log.error("WorkSpace id not found");
                    new NotFoundException("WorkSpace id not found!");
                });
        if (workSpace.getRoles().contains(userWorkSpaceRoleRepository.getUserIdAndWorkSpaceId(user.getId(), workSpace.getId()))) {
            Board board = new Board();
            board.setTitle(boardRequest.getTitle());
            board.setBackGround(boardRequest.getBackGround());
            board.setWorkSpace(workSpace);
            workSpace.getBoards().add(board);
            boardRepository.save(board);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Board successfully saved")
                    .build();
        } else throw new BadRequestException("user not in workSpace");
    }

    @Override
    public SimpleResponse updateBoard(BoardRequest boardRequest, Long boardId) {
        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("Board id not found");
                            throw new NotFoundException("Board id not found");
                        });
        WorkSpace workSpace = workspaceRepository.findById(boardRequest.workspaceId())
                .orElseThrow(() -> {
                    log.error("WorkSpace id not found");
                    throw new NotFoundException("WorkSpace id not found");
                });
        if (workSpace.getRoles().contains(userWorkSpaceRoleRepository.getUserIdAndWorkSpaceId(user.getId(), workSpace.getId()))) {
            board.setTitle(boardRequest.getTitle());
            board.setBackGround(boardRequest.getBackGround());
            boardRepository.save(board);
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Board updated successfully ")
                    .build();
        } else {
            throw new NotFoundException("In this workSpace not found user");
        }
    }

    @Override
    public SimpleResponse deleteBoard(Long boardId) {
        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("board id not found");
                    throw new NotFoundException("Board id not found");
                });
        WorkSpace workSpace = board.getWorkSpace();
        if (!workSpace.getBoards().contains(board)) {
            log.error("In workSpace not found board");
            throw new NotFoundException("In workSpace not found board");
        }
        if (workSpace.getRoles().contains(userWorkSpaceRoleRepository.getUserIdAndWorkSpaceId(user.getId(), workSpace.getId()))) {
            if (board.getFavorite() != null) {
                user.getFavorites().remove(board.getFavorite());
            }
            workSpace.getBoards().remove(board);
            boardRepository.deleteById(boardId);
        }
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
                    log.error("Board id not found");
                   throw  new NotFoundException("Board id not found");
                });
        boolean isFavorite = false;
        if (board.getFavorite() != null) {
            for (Favorite favorite : user.getFavorites()) {
                if (board.getFavorite().equals(favorite)) {
                    isFavorite = true;
                    break;
                }
            }
        }
        return BoardResponse.builder()
                .id(boardId)
                .title(board.getTitle())
                .backGround(board.getBackGround())
                .isFavorite(isFavorite)
                .build();
    }
}

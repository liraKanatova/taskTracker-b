package peaksoft.house.tasktrackerb9.services.impl;

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
import peaksoft.house.tasktrackerb9.repositories.BoardRepository;
import peaksoft.house.tasktrackerb9.repositories.UserWorkSpaceRoleRepository;
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

    private final UserWorkSpaceRoleRepository userWorkSpaceRoleRepository;

    private final JwtService jwtService;

    @Override
    public List<BoardResponse> getAllBoardsByWorkspaceId(Long workSpaceId) {
        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workspaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: "+workSpaceId+" not found");
                    throw new NotFoundException("WorkSpace with id: "+workSpaceId+" not found");
                });
        if (workSpace.getUserWorkSpaceRoles().contains(userWorkSpaceRoleRepository.getUserIdAndWorkSpaceId(user.getId(), workSpaceId))) {
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
        WorkSpace workSpace = workspaceRepository.findById(boardRequest.getWorkspaceId())
                .orElseThrow(() -> {
                    log.error("WorkSpace id not found");
                    new NotFoundException("WorkSpace id not found!");
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
                            log.error("Board id not found");
                            throw new NotFoundException("Board id not found");
                        });
        WorkSpace workSpace = workspaceRepository.findById(board.getWorkSpace().getId())
                .orElseThrow(() -> {
                    log.error("WorkSpace id not found");
                    throw new NotFoundException("WorkSpace id not found");
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
                    log.error("board id not found");
                    throw new NotFoundException("Board id not found");
                });
        WorkSpace workSpace = board.getWorkSpace();
        if (workSpace !=null) {
            workSpace.getBoards().remove(board);
        }else {
            log.error("In workSpace not found board");
            throw new NotFoundException("In workSpace not found board");
        }

            boardRepository.deleteById(boardId);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Board successfully deleted")
                .build();
    }

    @Override
    public BoardResponse getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("Board id not found");
                   throw  new NotFoundException("Board id not found");
                });
        return BoardResponse.builder()
                .boardId(boardId)
                .title(board.getTitle())
                .backGround(board.getBackGround())
                .build();
    }
}

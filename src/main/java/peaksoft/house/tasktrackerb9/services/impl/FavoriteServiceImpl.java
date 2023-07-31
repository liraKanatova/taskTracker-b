package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.*;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Board;
import peaksoft.house.tasktrackerb9.models.Favorite;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.BoardRepository;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.repositories.FavoriteRepository;
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl.CustomFavoriteRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.FavoriteService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;

    private final FavoriteRepository favoriteRepository;

    private final CustomFavoriteRepositoryImpl customFavoriteRepo;

    private final WorkSpaceRepository workSpaceRepository;

    private final BoardRepository boardRepository;

    private final JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public BoardResponse saveBoardFavorite(Long boardId) {

        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("Board with id: " + boardId + " not found");
                    throw new NotFoundException("Board with id: " + boardId + " not found");
                });
        if (user.getFavorites() != null) {
            for (Favorite favorite : user.getFavorites()) {
                if (favorite.getBoard().equals(board)) {
                    user.getFavorites().remove(favorite);
                    favoriteRepository.deleteById(favorite.getId());
                    return BoardResponse.builder()
                            .boardId(board.getId())
                            .title(board.getTitle())
                            .backGround(board.getBackGround())
                            .isFavorite(false)
                            .build();
                }
            }
        }
        Favorite favorite = new Favorite();
        favorite.setBoard(board);
        favorite.setMember(user);
        user.getFavorites().add(favorite);
        favoriteRepository.save(favorite);
        return BoardResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .backGround(board.getBackGround())
                .isFavorite(true)
                .build();
    }

    @Override
    public WorkSpaceFavoriteResponse saveWorkSpaceFavorite(Long workSpaceId) {

        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + workSpaceId + " not found");
                    throw new NotFoundException("WorkSpace with id: " + workSpaceId + " not found");
                });
        List<Favorite> favorites = favoriteRepository.getFavoriteByMemberId(user.getId());
        boolean deleted = false;
        for (Favorite f : favorites) {
            if (f.getWorkSpace() != null) {
                user.getFavorites().remove(f);
                String sql = " DELETE FROM favorites WHERE id = ?";
                int s = jdbcTemplate.update(sql, f.getId());
                log.warn(String.valueOf(s));
                userRepository.save(user);
                deleted = true;
                break;
            }
        }
        if (deleted) {
            return WorkSpaceFavoriteResponse.builder()
                    .workSpaceId(workSpaceId)
                    .name(workSpace.getName())
                    .isFavorite(false)
                    .build();
        }
        Favorite favorite = new Favorite();
        favorite.setWorkSpace(workSpace);
        favorite.setMember(user);
        favoriteRepository.save(favorite);
        user.getFavorites().add(favorite);
        return WorkSpaceFavoriteResponse.builder()
                .workSpaceId(workSpaceId)
                .name(workSpace.getName())
                .isFavorite(true)
                .build();
    }

    @Override
    public FavoriteResponse getAllFavorites() {
        return customFavoriteRepo.getAll();
    }
}
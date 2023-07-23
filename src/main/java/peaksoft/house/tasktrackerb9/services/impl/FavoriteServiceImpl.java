package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteBoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteResponse;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteWorkSpaceResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Board;
import peaksoft.house.tasktrackerb9.models.Favorite;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.repositories.FavoriteRepository;
import peaksoft.house.tasktrackerb9.services.FavoriteService;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;

    private final FavoriteRepository favoriteRepository;

    private final WorkSpaceRepository workSpaceRepository;

    private final BoardRepository boardRepository;

    private final JwtService jwtService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public SimpleResponse saveBoardFavorite(Long boardId) {

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
                    return SimpleResponse.builder()
                            .status(HttpStatus.OK)
                            .message("deleted")
                            .build();
                }
            }
        }
        Favorite favorite = new Favorite();
        favorite.setBoard(board);
        favorite.setUser(user);
        favoriteRepository.save(favorite);
        user.getFavorites().add(favorite);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("saved")
                .build();
    }

    @Override
    public SimpleResponse saveWorkSpaceFavorite(Long workSpaceId) {

        User user = jwtService.getAuthentication();
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + workSpaceId + " not found");
                    throw new NotFoundException("WorkSpace with id: " + workSpaceId + " not found");
                });
        List<Favorite> favorites = favoriteRepository.getFavoriteByUserId(user.getId());
        boolean deleted = false;
        for (Favorite f : favorites) {
            if (f.getWorkSpace() != null) {
                user.getFavorites().remove(f);
                String sql = " DELETE FROM favorite WHERE id = ?";
                int s = jdbcTemplate.update(sql, f.getId());
                log.warn(String.valueOf(s));
                userRepository.save(user);
                deleted = true;
                break;
            }
        }
        if (deleted) {
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Deleted")
                    .build();
        }
        Favorite favorite = new Favorite();
        favorite.setWorkSpace(workSpace);
        favorite.setUser(user);
        favoriteRepository.save(favorite);
        user.getFavorites().add(favorite);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("saved")
                .build();

    }

    @Override
    public List<FavoriteResponse> getAllFavorites() {

        User user = jwtService.getAuthentication();
        List<FavoriteBoardResponse> favoritesBoard = new ArrayList<>(List.of(favoriteRepository.getFavoriteByBoard(user.getId())));
        List<FavoriteWorkSpaceResponse> favoriteWorkSpace = new ArrayList<>(List.of(favoriteRepository.getFavoriteByWorkSpace(user.getId())));
        FavoriteResponse favorites = new FavoriteResponse();
        favorites.setBoardResponses(favoritesBoard);
        favorites.setWorkSpaceResponses(favoriteWorkSpace);
        List<FavoriteResponse> favoriteResponseList = new ArrayList<>();
        favoriteResponseList.add(favorites);
        return favoriteResponseList;

    }
}
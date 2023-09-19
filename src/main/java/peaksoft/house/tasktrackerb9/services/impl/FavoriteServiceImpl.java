package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.config.security.JwtService;
import peaksoft.house.tasktrackerb9.dto.response.*;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Board;
import peaksoft.house.tasktrackerb9.models.Favorite;
import peaksoft.house.tasktrackerb9.models.User;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.BoardRepository;
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

    private final FavoriteRepository favoriteRepository;
    private final CustomFavoriteRepositoryImpl customFavoriteRepo;
    private final WorkSpaceRepository workSpaceRepository;
    private final BoardRepository boardRepository;
    private final JwtService jwtService;

    @Override
    public BoardResponse saveBoardFavorite(Long boardId) {
        User user = jwtService.getAuthentication();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("Board with id: " + boardId + " not found");
                    return new NotFoundException("Board with id: " + boardId + " not found");
                });
        List<Favorite> favorites = user.getFavorites();
        if (favorites != null) {
            for (Favorite favorite : favorites) {
                if (favorite.getBoard() != null && favorite.getBoard().equals(board)) {
                    favorites.remove(favorite);
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
        assert favorites != null;
        favorites.add(favorite);
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
                    return new NotFoundException("WorkSpace with id: " + workSpaceId + " not found");
                });
        List<Favorite> favorites = user.getFavorites();
        boolean isFavorite = false;
        Favorite existingWorkSpaceFavorite = null;
        if (favorites != null) {
            for (Favorite favorite : favorites) {
                if (favorite.getWorkSpace() != null && favorite.getWorkSpace().equals(workSpace)) {
                    existingWorkSpaceFavorite = favorite;
                    isFavorite = true;
                    break;
                }
            }
        }

        if (isFavorite) {
            favorites.remove(existingWorkSpaceFavorite);
            favoriteRepository.deleteById(existingWorkSpaceFavorite.getId());
        } else {
            Favorite newWorkSpaceFavorite = new Favorite();
            newWorkSpaceFavorite.setWorkSpace(workSpace);
            newWorkSpaceFavorite.setMember(user);
            assert favorites != null;
            favorites.add(newWorkSpaceFavorite);
            favoriteRepository.save(newWorkSpaceFavorite);
        }
        return WorkSpaceFavoriteResponse.builder()
                .workSpaceId(workSpace.getId())
                .name(workSpace.getName())
                .isFavorite(!isFavorite)
                .build();
    }

    @Override
    public FavoriteResponse getAllFavorites() {
        return customFavoriteRepo.getAll();
    }
}
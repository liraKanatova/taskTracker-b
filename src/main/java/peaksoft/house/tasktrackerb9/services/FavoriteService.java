package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceFavoriteResponse;


public interface FavoriteService {

    BoardResponse saveBoardFavorite(Long boardId);

    WorkSpaceFavoriteResponse saveWorkSpaceFavorite(Long workSpaceId);

    FavoriteResponse getAllFavorites();
}

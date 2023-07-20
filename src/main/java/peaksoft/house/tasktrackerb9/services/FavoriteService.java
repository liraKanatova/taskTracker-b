package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.response.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    SimpleResponse saveBoardFavorite(Long boardId);

    SimpleResponse saveWorkSpaceFavorite(Long workSpaceId);

    List<FavoriteResponse> getAllFavorites();
}

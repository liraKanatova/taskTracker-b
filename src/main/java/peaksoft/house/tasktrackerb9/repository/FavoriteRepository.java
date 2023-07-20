package peaksoft.house.tasktrackerb9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteBoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteWorkSpaceResponse;
import peaksoft.house.tasktrackerb9.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

@Query("select new peaksoft.house.tasktrackerb9.dto.response.FavoriteBoardResponse(f.id,f.board.id,f.board.title,f.board.backGround)from Favorite f where f.user.id=:userId")
FavoriteBoardResponse getFavoriteByBoard(Long userId);

@Query("select new peaksoft.house.tasktrackerb9.dto.response.FavoriteWorkSpaceResponse(f.id,f.workSpace.id,f.workSpace.name)from Favorite f where f.user.id=:userId")
FavoriteWorkSpaceResponse getFavoriteByWorkSpace(Long userId);
}
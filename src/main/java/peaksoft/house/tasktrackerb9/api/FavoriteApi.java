package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteResponse;
import peaksoft.house.tasktrackerb9.services.FavoriteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/favorites")
@Tag(name = "Favourite Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoriteApi {

    private final FavoriteService favoriteService;

    @Operation(summary = "Save favorite",description = "Save favorite by board")
    @PostMapping("/board/{boardId}")
    public SimpleResponse saveBoardFavorite(@PathVariable Long boardId){
        return favoriteService.saveBoardFavorite(boardId);
    }

    @Operation(summary = "Save favorite",description = "Save favorite by workSpace")
    @PostMapping("/work-Space/{workSpaceId}")
    public SimpleResponse saveWorkSpaceFavorite(@PathVariable Long workSpaceId){
        return favoriteService.saveWorkSpaceFavorite(workSpaceId);
    }

    @Operation(summary = "All favorites",description = "All favorites in board and workSpace")
    @GetMapping
    public List<FavoriteResponse> getAllFavorites(){
        return favoriteService.getAllFavorites();
    }
}

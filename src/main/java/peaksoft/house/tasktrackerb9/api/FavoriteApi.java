package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.BoardResponse;
import peaksoft.house.tasktrackerb9.dto.response.FavoriteResponse;
import peaksoft.house.tasktrackerb9.dto.response.WorkSpaceFavoriteResponse;
import peaksoft.house.tasktrackerb9.services.FavoriteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
@Tag(name = "Favourite Api", description = "Api methods for favorites")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAuthority('ADMIN')")
public class FavoriteApi {

    private final FavoriteService favoriteService;

    @Operation(summary = "Save favorite", description = "Make and delete board favorites")
    @PostMapping("/board/{boardId}")
    public BoardResponse saveBoardFavorite(@PathVariable Long boardId) {
        return favoriteService.saveBoardFavorite(boardId);
    }

    @Operation(summary = "Save favorite", description = "Make and delete work_space favorites")
    @PostMapping("/work_space/{workSpaceId}")
    public WorkSpaceFavoriteResponse saveWorkSpaceFavorite(@PathVariable Long workSpaceId) {
        return favoriteService.saveWorkSpaceFavorite(workSpaceId);
    }

    @Operation(summary = "All favorites", description = "All favorites in board and workSpace")
    @GetMapping
    public FavoriteResponse getAllFavorites() {
        return favoriteService.getAllFavorites();
    }
}

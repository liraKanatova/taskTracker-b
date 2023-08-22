package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.ItemRequest;
import peaksoft.house.tasktrackerb9.dto.response.ItemResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.ItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Item API", description = "API for managing items")
public class ItemApi {

    private final ItemService itemService;

    @PostMapping("/save")
    @Operation(summary = "Save item", description = "save item by checkList id")
    public ItemResponse saveItem(@RequestBody ItemRequest itemRequest) {
        return itemService.saveItem(itemRequest);
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "Add to completed", description = "Add to completed item and remove from completed by item id")
    public ItemResponse addToCompletedAndRemoveFromCompleted(@PathVariable Long itemId) {
        return itemService.addToCompletedAndRemoveFromCompleted(itemId);
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete item", description = "delete item by item id")
    public SimpleResponse deleteItem(@PathVariable Long itemId) {
        return itemService.deleteItem(itemId);
    }
}

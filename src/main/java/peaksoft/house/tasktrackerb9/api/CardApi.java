package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.CardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
@Tag(name = "Card API", description = "API for managing cards")
@CrossOrigin(origins = "*",maxAge = 3600)
public class CardApi {

    private final CardService cardService;

    @Operation(summary = "Archive a card", description = "archiving and unarchiving card by id")
    @PutMapping("/archive/{cardId}")
    public SimpleResponse archiveCard(@PathVariable Long cardId) {
        return cardService.archiveCard(cardId);
    }

    @Operation(summary = "Archive all cards", description = "Archives all cards in a specific column by id")
    @PutMapping("/all-archive/{columnId}")
    public SimpleResponse archiveAllCardsInColumn(@PathVariable Long columnId) {
        return cardService.archiveAllCardsInColumn(columnId);
    }

    @Operation(summary = "Delete all cards", description = "This operation allows deleting all cards in a column by its ID")
    @DeleteMapping("/all/{columnId}")
    public SimpleResponse deleteAllCardsInColumn(@PathVariable Long columnId) {
        return cardService.deleteAllCardsInColumn(columnId);
    }

}
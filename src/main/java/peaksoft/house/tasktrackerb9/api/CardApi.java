package peaksoft.house.tasktrackerb9.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peaksoft.house.tasktrackerb9.dto.request.CardRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateCardRequest;
import peaksoft.house.tasktrackerb9.dto.response.CardInnerPageResponse;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.services.CardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
@Tag(name = "Card API", description = "API for managing cards")
@CrossOrigin(origins = "*",maxAge = 3600)
public class CardApi {

    private final CardService cardService;

    @GetMapping("/{cardId}")
    @Operation(summary = "Get card", description = "get inner page card by card id")
    public CardInnerPageResponse getInnerPageCardById(@PathVariable Long cardId){
        return cardService.getInnerPageCardById(cardId);
    }

    @GetMapping("/get-cards/{columnId}")
    @Operation(summary = "Get all cards", description = "get all cards by column id")
    public List<CardResponse> getCardsByColumnId(@PathVariable Long columnId){
        return cardService.getAllCardsByColumnId(columnId);
    }

    @PostMapping
    @Operation(summary = "Save card",description = "save card by column id")
    public CardInnerPageResponse saveCard(@RequestBody CardRequest cardRequest){
        return cardService.saveCard(cardRequest);
    }

    @PutMapping
    @Operation(summary = "Update card",description = "update card with card id")
    public SimpleResponse updateCard(@RequestBody UpdateCardRequest updateCardRequest){
        return cardService.updateCardById(updateCardRequest);
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "Delete card",description = "delete card with card id")
    public SimpleResponse deleteCard(@PathVariable Long cardId){
        return cardService.deleteCard(cardId);
    }

}
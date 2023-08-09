package peaksoft.house.tasktrackerb9.dto.request;

public record UpdateCardRequest(

        Long cardId,

        String title,

        String description) {
}
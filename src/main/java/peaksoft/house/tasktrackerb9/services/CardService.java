package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

public interface CardService {

    SimpleResponse archiveCard(Long cardId);

    SimpleResponse deleteAllCardsInColumn(Long columnId);

    SimpleResponse archiveAllCardsInColumn(Long columnId);

}
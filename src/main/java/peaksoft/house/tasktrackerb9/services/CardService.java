package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.CardRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateCardRequest;
import peaksoft.house.tasktrackerb9.dto.response.CardInnerPageResponse;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

import java.util.List;

public interface CardService {

    CardInnerPageResponse saveCard(CardRequest cardRequest);

    CardInnerPageResponse getInnerPageCardById(Long cardId);

    List<CardResponse> getAllCardsByColumnId(Long columnId);

    SimpleResponse updateCardById(UpdateCardRequest updateCardRequest);

    SimpleResponse deleteCard(Long id);

}
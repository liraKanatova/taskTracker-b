package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService;

import peaksoft.house.tasktrackerb9.dto.response.CardInnerPageResponse;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.models.Card;

import java.util.List;

public interface CustomCardJdbcTemplateService {

    CardInnerPageResponse getAllCardInnerPage(Long cardId);

    List<CardResponse> getAllCardsByColumnId(Long columnId);

    CardInnerPageResponse convertToCardInnerPageResponse(Card card);

}
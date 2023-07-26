package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.LabelRequest;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Label;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.LabelRepository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.LabelJdbcTemplateService;
import peaksoft.house.tasktrackerb9.services.LabelService;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final CardRepository cardRepository;
    private final LabelJdbcTemplateService labelJdbcTemplateService;

    @Override
    public List<LabelResponse> getAllLabels() {
       return labelJdbcTemplateService.getAllLabels();
    }

    @Override
    public SimpleResponse saveLabels(LabelRequest labelRequest) {
        Label label = new Label(labelRequest.labelName(), labelRequest.color());
        labelRepository.save(label);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Label with name : %s  successfully saved!", label.getLabelName()))
                .build();
    }
    @Override
    public SimpleResponse addLabelsToCard(Long cardId,Long labelId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new NotFoundException(String.format("Card with id : %s doesn't exist!", cardId)));
        Label label=labelRepository.findById(labelId).orElseThrow(() ->
                new NotFoundException(String.format("Label with id : %s doesn't exist!", labelId)));
        List<Card> cards = label.getCards();
        if (cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
        label.setCards(cards);
        List<Label> labels = card.getLabels();
        if (labels == null) {
            labels = new ArrayList<>();
        }
        labels.add(label);
        card.setLabels(labels);
        labelRepository.save(label);
        cardRepository.save(card);

         return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Label with name : %s  successfully added to card with name %s ", label.getLabelName(),card.getTitle()))
                .build();
    }
    @Override
    public LabelResponse getLabelById(Long labelId) {
        return labelJdbcTemplateService.getLabelById(labelId);
    }
    @Override
    public SimpleResponse updateLabelDeleteById(Long labelId,LabelRequest labelRequest) {
        Label label=labelRepository.findById(labelId).orElseThrow(()->
                new NotFoundException(String.format("Label with id: %s doesn't exist !", labelId)));
        label.setLabelName(labelRequest.labelName());
        label.setColor(labelRequest.color());
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Label with name : %s  successfully updated!", label.getLabelName()))
                .build();
    }
    @Override
    public SimpleResponse deleteLabelById(Long labelId) {
        Label label=labelRepository.findById(labelId).orElseThrow(()->
                new NotFoundException(String.format("Label with id: %s doesn't exist !", labelId)));
        labelRepository.delete(label);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Label with name : %s  successfully deleted!", label.getLabelName()))
                .build();
    }
    @Override
    public List<LabelResponse> getAllLabelsByCardId(Long cardId) {
        return labelJdbcTemplateService.getAllLabelsByCardId(cardId);
    }
}

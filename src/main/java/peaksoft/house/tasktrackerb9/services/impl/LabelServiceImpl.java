package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.request.LabelRequest;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;
import peaksoft.house.tasktrackerb9.exceptions.AlreadyExistException;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.Label;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.LabelRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomLabelRepository;
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
    private final CustomLabelRepository labelJdbcTemplateService;

    @Override
    public List<LabelResponse> getAllLabels() {
        return labelJdbcTemplateService.getAllLabels();
    }

    @Override
    public SimpleResponse saveLabels(LabelRequest labelRequest) {
        Label label = new Label(labelRequest.description(), labelRequest.color());
        labelRepository.save(label);
        log.info(String.format("Label with name : %s  successfully saved!", label.getLabelName()));
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Label with name : %s  successfully saved!", label.getLabelName()))
                .build();
    }

    @Override
    public SimpleResponse addLabelsToCard(Long cardId, Long labelId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error(String.format("Card with id : %s doesn't exist!", cardId));
            return new NotFoundException(String.format("Card with id : %s doesn't exist!", cardId));
        });
        Label label = labelRepository.findById(labelId).orElseThrow(() -> {
            log.error(String.format("Label with id : %s doesn't exist!", labelId));
            return new NotFoundException(String.format("Label with id : %s doesn't exist!", labelId));
        });

        List<Card> cards = label.getCards();
        boolean labelFound = false;

        for (Card c : cards) {
            if (c.getLabels().stream().anyMatch(l -> l.getLabelName().equals(label.getLabelName()) && l.getColor().equals(label.getColor()))) {
                labelFound = true;
                break;
            }
        }

        if (!labelFound) {
            throw new AlreadyExistException(String.format("Label with name '%s' and color '%s' already exists in a card!", label.getLabelName(), label.getColor()));
        } else {
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
            log.info(String.format("Label with name : %s  successfully added to card with name %s ", label.getLabelName(), card.getTitle()));
            return SimpleResponse.builder()
                    .status(HttpStatus.OK)
                    .message(String.format("Label with name : %s  successfully added to card with name %s ", label.getLabelName(), card.getTitle()))
                    .build();
        }
    }
    @Override
    public LabelResponse getLabelById(Long labelId) {
        return labelJdbcTemplateService.getLabelById(labelId);
    }

    @Override
    public SimpleResponse updateLabelDeleteById(Long labelId, LabelRequest labelRequest) {
        Label label = labelRepository.findById(labelId).orElseThrow(() -> {
            log.error(String.format("Label with id: %s doesn't exist !", labelId));
            return new NotFoundException(String.format("Label with id: %s doesn't exist !", labelId));
        });
        log.info(String.format("Label with name : %s  successfully updated!", label.getLabelName()));
        label.setLabelName(labelRequest.description());
        label.setColor(labelRequest.color());
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Label with name : %s  successfully updated!", label.getLabelName()))
                .build();
    }

    @Override
    public SimpleResponse deleteLabelById(Long labelId) {
        Label label = labelRepository.findById(labelId).orElseThrow(() -> {
            log.error(String.format("Label with id: %s doesn't exist !", labelId));
            return new NotFoundException(String.format("Label with id: %s doesn't exist !", labelId));
        });
        labelRepository.delete(label);
        log.info(String.format("Label with name : %s  successfully deleted!", label.getLabelName()));
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

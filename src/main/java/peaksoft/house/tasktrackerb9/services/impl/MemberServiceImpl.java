package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.models.Card;
import peaksoft.house.tasktrackerb9.models.WorkSpace;
import peaksoft.house.tasktrackerb9.repositories.CardRepository;
import peaksoft.house.tasktrackerb9.repositories.WorkSpaceRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl.CustomMemberRepositoryImpl;
import peaksoft.house.tasktrackerb9.services.MemberService;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final WorkSpaceRepository workSpaceRepository;
    private final CardRepository cardRepository;
    private final CustomMemberRepositoryImpl customMemberRepository;

    @Override
    public List<MemberResponse> searchByEmail(Long workSpaceId, String email) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId)
                .orElseThrow(() -> {
                    log.error("WorkSpace with id: " + workSpaceId + " not found");
                    throw new NotFoundException("WorkSpace with id: " + workSpaceId + " not found");
                });
        return customMemberRepository.searchByEmail(workSpace.getId(), email);
    }

    @Override
    public AllMemberResponse getAll(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> {
            log.error("Card with id: " + cardId + " not found");
            throw new NotFoundException("Card with id: " + cardId + " not found");
        });
        return customMemberRepository.getAll(card.getId());
    }
}
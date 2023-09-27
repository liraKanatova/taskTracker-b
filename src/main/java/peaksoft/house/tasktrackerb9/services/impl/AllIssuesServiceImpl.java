package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;
import peaksoft.house.tasktrackerb9.repositories.CheckListRepository;
import peaksoft.house.tasktrackerb9.repositories.LabelRepository;
import peaksoft.house.tasktrackerb9.repositories.UserRepository;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomAllIssuesRepository;
import peaksoft.house.tasktrackerb9.services.AllIssuesService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AllIssuesServiceImpl implements AllIssuesService {

    private final CustomAllIssuesRepository customAllIssuesRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final CheckListRepository checkListRepository;

    @Override
    public List<AllIssuesResponse> filterIssues(Long workSpaceId, LocalDate from, LocalDate to, List<Long> labelIds, List<Long> assigneeMemberIds) {

        List<AllIssuesResponse> allIssuesResponses = customAllIssuesRepository.filterIssues(workSpaceId, from, to, labelIds, assigneeMemberIds);

        for (AllIssuesResponse allIssuesResponse : allIssuesResponses) {
            allIssuesResponse.setIsChecked(checkListRepository.doesCheckListExistForCard(allIssuesResponse.getCardId()));
            allIssuesResponse.setLabelResponses(labelRepository.findAllByCardId(allIssuesResponse.getCardId()));
            allIssuesResponse.setAssignee(userRepository.findAllParticipantByCardId(allIssuesResponse.getCardId()));
        }

        return allIssuesResponses;
    }
}
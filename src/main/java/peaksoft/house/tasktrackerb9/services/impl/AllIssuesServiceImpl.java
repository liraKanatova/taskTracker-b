package peaksoft.house.tasktrackerb9.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomAllIssuesRepository;
import peaksoft.house.tasktrackerb9.services.AllIssuesService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AllIssuesServiceImpl implements AllIssuesService {

    private final CustomAllIssuesRepository customAllIssuesRepository;

    @Override
    public List<AllIssuesResponse> filterIssues(Long workSpaceId, Date from, Date to, List<String> labelResponses, List<String> assigneeSearchQueries) {
        return customAllIssuesRepository.filterIssues(workSpaceId, from, to, labelResponses, assigneeSearchQueries);
    }
}

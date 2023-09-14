package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;

import java.time.LocalDate;
import java.util.List;

public interface CustomAllIssuesRepository {

    List<AllIssuesResponse> filterIssues(
            Long workSpaceId,
            LocalDate from,
            LocalDate to,
            List<Long> labelIds,
            List<Long> assigneeIds);

}
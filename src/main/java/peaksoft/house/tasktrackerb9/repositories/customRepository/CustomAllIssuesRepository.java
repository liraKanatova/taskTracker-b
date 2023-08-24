package peaksoft.house.tasktrackerb9.repositories.customRepository;

import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;

import java.util.Date;
import java.util.List;

public interface CustomAllIssuesRepository {

     List<AllIssuesResponse> filterIssues(
            Long workSpaceId,
            Date from,
            Date to,
            List<String> labelResponses,
            List<String> assigneeSearchQueries);

}
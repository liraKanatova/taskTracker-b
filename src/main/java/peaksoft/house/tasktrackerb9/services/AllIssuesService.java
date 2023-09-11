package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AllIssuesService {

    List<AllIssuesResponse> filterIssues(
            Long workSpaceId,
            LocalDate from,
            LocalDate to,
            List<Long> labelIds,
            List<Long> assigneeMemberIds);

}
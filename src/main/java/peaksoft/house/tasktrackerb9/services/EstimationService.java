package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.EstimationRequest;
import peaksoft.house.tasktrackerb9.dto.request.UpdateEstimationRequest;
import peaksoft.house.tasktrackerb9.dto.response.EstimationResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

public interface EstimationService {

    EstimationResponse createdEstimation(EstimationRequest request);

    SimpleResponse updateEstimation(Long estimationId, UpdateEstimationRequest request);
}

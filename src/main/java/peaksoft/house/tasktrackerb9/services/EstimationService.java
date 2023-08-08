package peaksoft.house.tasktrackerb9.services;


import peaksoft.house.tasktrackerb9.dto.request.EstimationRequest;
import peaksoft.house.tasktrackerb9.dto.response.EstimationResponse;

public interface EstimationService {

    EstimationResponse createdEstimation(EstimationRequest request);

    EstimationResponse updateEstimation(EstimationRequest request);
}

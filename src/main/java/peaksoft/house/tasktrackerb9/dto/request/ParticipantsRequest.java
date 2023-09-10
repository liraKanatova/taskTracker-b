package peaksoft.house.tasktrackerb9.dto.request;

import lombok.*;
import peaksoft.house.tasktrackerb9.enums.Role;

@Builder
public record ParticipantsRequest(Long workSpacesId,String email,String link){

}

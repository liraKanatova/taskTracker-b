package peaksoft.house.tasktrackerb9.dto.request;

import lombok.Builder;

@Builder
public record UserRequest(String firstName, String lastName, String email, String password, String repeatPassword) {

}
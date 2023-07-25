package peaksoft.house.tasktrackerb9.dto.request;

import lombok.*;


@Builder
public record BoardRequest (

        String title,

        String backGround,

        Long workSpaceId
){
}

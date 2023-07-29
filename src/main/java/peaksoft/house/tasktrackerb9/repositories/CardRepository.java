package peaksoft.house.tasktrackerb9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.models.Card;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {

     Optional<CardResponse> findCardById(Long id);

}
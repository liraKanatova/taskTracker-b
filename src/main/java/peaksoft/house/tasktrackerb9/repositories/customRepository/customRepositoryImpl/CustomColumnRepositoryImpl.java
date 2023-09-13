package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.CardResponse;
import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomColumnRepository;

import java.util.*;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomColumnRepositoryImpl implements CustomColumnRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ColumnResponse> getAllColumns(Long boardId) {
        String sql = """
                SELECT c.id, c.title, c.is_archive, c.board_id  AS boardId, card.id AS card_id, card.title AS card_title
                FROM columns c
                LEFT JOIN cards card ON c.id = card.column_id
                WHERE c.board_id = ?
                ORDER BY c.id
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long columnId = rs.getLong("id");
            String title = rs.getString("title");
            Boolean isArchive = rs.getBoolean("is_archive");
            Long fetchedBoardId = rs.getLong("boardId");
            Long cardId = rs.getLong("card_id");
            String cardTitle = rs.getString("card_title");
            ColumnResponse columnResponse = new ColumnResponse(columnId, title, isArchive, fetchedBoardId, null);
            CardResponse cardResponse = new CardResponse(cardId, cardTitle);
            columnResponse.setCardResponses(Collections.singletonList(cardResponse));
            return columnResponse;
        }, boardId);
    }
    }

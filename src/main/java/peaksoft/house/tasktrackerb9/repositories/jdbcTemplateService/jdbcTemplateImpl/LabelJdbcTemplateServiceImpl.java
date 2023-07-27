package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.LabelJdbcTemplateService;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j
public class LabelJdbcTemplateServiceImpl implements LabelJdbcTemplateService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<LabelResponse> getAllLabels() {
        String query = """
                SELECT  l.id AS  id,
                l.label_name AS  labelName,
                l.color   AS  labelColor
                FROM labels AS l
                """;
        List<LabelResponse> labelResponses = jdbcTemplate.query(query, ((rs, rowNum) -> {
            LabelResponse labelResponse = new LabelResponse();
            labelResponse.setLabelId(rs.getLong("id"));
            labelResponse.setLabelName(rs.getString("labelName"));
            labelResponse.setLabelColor(rs.getString("labelColor"));
            return labelResponse;
        }));
        return labelResponses;
    }

    @Override
    public List<LabelResponse> getAllLabelsByCardId(Long cardId) {
        String query = """
                SELECT   l.id AS id,
                l.label_name AS labelName,
                l.color AS labelColor
                FROM labels AS
                l JOIN labels_cards lc ON l.id = lc.labels_id 
                WHERE lc.cards_id=?
                """;
        List<LabelResponse> labelResponses =
                jdbcTemplate.query(query, new Object[]{cardId}, (rs, rowNum) -> {
                    LabelResponse labelResponse = new LabelResponse();
                    labelResponse.setLabelId(rs.getLong("id"));
                    labelResponse.setDescription(rs.getString("labelName"));
                    labelResponse.setColor(rs.getString("labelColor"));
                    return labelResponse;
                });
        if (labelResponses.isEmpty()) {
            log.error(String.format("Card with id: %s doesn't exist ", cardId));
            throw new NotFoundException(String.format("Card with id: %s doesn't exist ", cardId));
        }
        return labelResponses;
    }

    @Override
    public LabelResponse getLabelById(Long labelId) {
        String query = """
                 SELECT   l.id AS  id,
                 l.label_name AS  labelName,
                 l.color AS  labelColor 
                 FROM labels AS l where l.id=?
                """;
        Optional<LabelResponse> optionalLabelResponse = Optional.ofNullable(
                jdbcTemplate.queryForObject(query, new Object[]{labelId}, ((rs, rowNum) -> {
                    LabelResponse labelResponse1 = new LabelResponse();
                    labelResponse1.setLabelId(rs.getLong("id"));
                    labelResponse1.setDescription(rs.getString("labelName"));
                    labelResponse1.setColor(rs.getString("labelColor"));
                    return labelResponse1;
                })));
        return optionalLabelResponse.orElseThrow(() -> {
            log.error(String.format("Label with id :%s doesn't exist !", labelId));
            return new NotFoundException(String.format("Label with id :%s doesn't exist !", labelId));
        });
    }
}

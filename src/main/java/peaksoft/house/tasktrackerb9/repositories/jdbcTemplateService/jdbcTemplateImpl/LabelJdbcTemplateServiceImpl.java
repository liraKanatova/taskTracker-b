package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class LabelJdbcTemplateServiceImpl implements LabelJdbcTemplateService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<LabelResponse> getAllLabels() {
        List<LabelResponse> labelResponses = jdbcTemplate.query(getAllLabelsQuery(), ((rs, rowNum) -> {
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
        List<LabelResponse> labelResponses =
                jdbcTemplate.query(getLabelByCardIdQuery(), new Object[]{cardId}, (rs, rowNum) -> {
                    LabelResponse labelResponse = new LabelResponse();
                    labelResponse.setLabelId(rs.getLong("id"));
                    labelResponse.setLabelName(rs.getString("labelName"));
                    labelResponse.setLabelColor(rs.getString("labelColor"));
                    return labelResponse;
                });
        if (labelResponses.isEmpty()) {
            throw new NotFoundException(String.format("Card with id: %s doesn't exist ",cardId));
        }
        return labelResponses;
    }

    @Override
    public LabelResponse getLabelById(Long labelId) {
        Optional<LabelResponse> optionalLabelResponse = Optional.ofNullable(
                jdbcTemplate.queryForObject(getLabelByIdQuery(), new Object[]{labelId}, ((rs, rowNum) -> {
                    LabelResponse labelResponse1 = new LabelResponse();
                    labelResponse1.setLabelId(rs.getLong("id"));
                    labelResponse1.setLabelName(rs.getString("labelName"));
                    labelResponse1.setLabelColor(rs.getString("labelColor"));
                    return labelResponse1;
                })));
        return optionalLabelResponse.orElseThrow(() ->
                new NotFoundException(String.format("Label with id :%s doesn't exist !", labelId)));
    }

    private String getAllLabelsQuery() {
        String sql = "SELECT l.id AS id,l.label_name AS labelName,l.color AS labelColor FROM labels AS l";
        return sql;
    }

    private String getLabelByIdQuery() {
        String sql = "  SELECT l.id AS id,l.label_name AS labelName,l.color AS labelColor FROM labels AS l where l.id=?";
        return sql;
    }

    private String getLabelByCardIdQuery() {
        String sql = "  SELECT l.id AS id,l.label_name AS labelName,l.color AS labelColor FROM labels AS\n" +
                "      l JOIN labels_cards lc on l.id = lc.labels_id where lc.cards_id=?";
        return sql;
    }
}

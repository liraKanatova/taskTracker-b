package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.AttachmentResponse;
import peaksoft.house.tasktrackerb9.exceptions.NotFoundException;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomAttachmentRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomAttachmentRepositoryImpl implements CustomAttachmentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AttachmentResponse> getAttachmentsByCardId(Long cardId) {
        String sql = """
                    SELECT
                        a.id,
                        a.document_link,
                        a.created_at
                    FROM attachments a
                    WHERE a.card_id = ?;
                """;
        List<AttachmentResponse> attachments = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new AttachmentResponse(
                        rs.getLong("id"),
                        rs.getString("document_link"),
                        (rs.getString("created_at"))),
                cardId
        );
        if (attachments.isEmpty()) {
            throw new NotFoundException("Attachments not found for card with id: " + cardId);
        }
        return attachments;
    }
}
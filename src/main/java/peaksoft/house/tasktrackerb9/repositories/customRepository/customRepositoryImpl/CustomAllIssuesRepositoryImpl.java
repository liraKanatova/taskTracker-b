package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import peaksoft.house.tasktrackerb9.dto.response.AllIssuesResponse;
import peaksoft.house.tasktrackerb9.dto.response.LabelResponse;
import peaksoft.house.tasktrackerb9.dto.response.UserAllIssuesResponse;
import peaksoft.house.tasktrackerb9.exceptions.BadCredentialException;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomAllIssuesRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class CustomAllIssuesRepositoryImpl implements CustomAllIssuesRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<AllIssuesResponse> filterIssues(
            Long workSpaceId,
            Date from,
            Date to,
            List<String> labelResponses,
            List<String> assigneeSearchQueries) {
        String sql = """
                SELECT         c.id as cardId,
                               c.created_date AS created,
                               DATE_PART('day', NOW() - c.created_date) AS period,
                               c.creator_id AS creatorUserId,
                               concat(u_creator.first_name, '  ', u_creator.last_name) AS fullNameCreatorCard,
                               col.title AS column_title,
                               u_user.id as userId,
                               concat(u_user.first_name, '   ', u_user.last_name) AS assignee,
                               u_user.image AS userImage,
                               l.id as labelId,
                               l.label_name AS description,
                               l.color AS color,
                               (SELECT COUNT(*) FROM check_lists chk WHERE chk.card_id = c.id) AS total_checklists,
                               (SELECT COUNT(*) FROM check_lists chk JOIN items i ON chk.id = i.check_list_id
                                WHERE chk.card_id = c.id AND i.is_done = true) AS completed_checklists,
                               c.description AS cardDescription,
                               (SELECT COUNT(c2.id) FROM cards c2 JOIN columns col2 ON c2.column_id = col2.id
                                JOIN boards b2 ON col2.board_id = b2.id WHERE b2.work_space_id = w.id) AS total_cards
                                FROM cards c
                                 INNER JOIN columns col ON c.column_id = col.id
                                 INNER JOIN boards b ON col.board_id = b.id
                                 INNER JOIN work_spaces w ON b.work_space_id = w.id
                                 LEFT JOIN cards_members cm ON c.id = cm.cards_id
                                 LEFT JOIN users u_user ON cm.members_id = u_user.id
                                 LEFT JOIN labels_cards lc ON c.id = lc.cards_id
                                 LEFT JOIN labels l ON lc.labels_id = l.id
                                 LEFT JOIN users u_creator ON c.creator_id = u_creator.id
                        WHERE w.id = :workSpaceId
                 """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("workSpaceId", workSpaceId);
        if (from != null && to != null) {
            if (from.after(to)) {
                log.error("Date of start must be after date of finish");
                throw new BadCredentialException("Date of start must be after date of finish");
            }
            sql += " AND  c.created_date BETWEEN :from AND :to ";
            parameters.addValue("from", from);
            parameters.addValue("to", to);
        }

        if (labelResponses != null && !labelResponses.isEmpty()) {
            for (int i = 0; i < labelResponses.size(); i++) {
                String color = labelResponses.get(i);
                String paramName = "color" + i;
                sql += " AND (l.color ILIKE :" + paramName + ") ";
                parameters.addValue(paramName, "%" + color + "%");
            }
        }

        if (assigneeSearchQueries != null && !assigneeSearchQueries.isEmpty()) {
            sql += " AND (";
            for (int i = 0; i < assigneeSearchQueries.size(); i++) {
                String paramName = "searchQuery" + i;
                String paramValue = "%" + assigneeSearchQueries.get(i) + "%";
                sql += "(" +
                        "u_user.first_name ILIKE :" + paramName + " OR " +
                        "u_user.last_name ILIKE :" + paramName + " OR " +
                        "u_user.email ILIKE :" + paramName + ")";
                parameters.addValue(paramName, paramValue);
                if (i < assigneeSearchQueries.size() - 1) {
                    sql += " OR ";
                }
            }
            sql += ")";
        }
        return namedParameterJdbcTemplate.query(sql, parameters, new AllIssuesResponseRowMapper());
    }

    private static class AllIssuesResponseRowMapper implements RowMapper<AllIssuesResponse> {
        @Override
        public AllIssuesResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            AllIssuesResponse allIssuesResponse = new AllIssuesResponse();
            allIssuesResponse.setCardId(rs.getLong("cardId"));
            allIssuesResponse.setCreated(rs.getDate("created"));
            allIssuesResponse.setDurationDay(rs.getString("period"));
            allIssuesResponse.setColumn(rs.getString("column_title"));
            allIssuesResponse.setCreatorId(rs.getLong("creatorUserId"));
            allIssuesResponse.setCreatorFullName(rs.getString("fullNameCreatorCard"));

            Long userId = rs.getLong("userId");
            if (!rs.wasNull()) {
                List<UserAllIssuesResponse> assignees = new ArrayList<>();
                UserAllIssuesResponse userAllIssuesResponse1 = new UserAllIssuesResponse();
                userAllIssuesResponse1.setUserId(userId);
                userAllIssuesResponse1.setFullName(rs.getString("assignee"));
                userAllIssuesResponse1.setImage(rs.getString("userImage"));
                assignees.add(userAllIssuesResponse1);
                allIssuesResponse.setAssignee(assignees);

            } else {
                allIssuesResponse.setAssignee(new ArrayList<>());
            }
            Long labelId = rs.getLong("labelId");
            if (!rs.wasNull()) {
                List<LabelResponse> labelResponse = new ArrayList<>();
                LabelResponse labelResponse1 = new LabelResponse();
                labelResponse1.setLabelId(labelId);
                labelResponse1.setDescription(rs.getString("description"));
                labelResponse1.setColor(rs.getString("color"));
                labelResponse.add(labelResponse1);
                allIssuesResponse.setLabelResponses(labelResponse);
            } else {
                allIssuesResponse.setLabelResponses(new ArrayList<>());
            }
            String result = rs.getString("completed_checklists") + "/" + rs.getString("total_checklists");
            allIssuesResponse.setCheckListResponse(result);
            allIssuesResponse.setDescription(rs.getString("cardDescription"));
            allIssuesResponse.setCountOfCards(rs.getInt("total_cards"));
            return allIssuesResponse;
        }
    }
}

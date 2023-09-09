package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.ColumnResponse;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.ColumnsJdbcTemplateService;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ColumnsJdbcTemplateServiceImpl implements ColumnsJdbcTemplateService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ColumnResponse> getAllColumns(Long boardId) {
        String sql = """
                select c.id,c.title,c.is_archive  from boards b
                join columns c on b.id = c.board_id
                where b.id=? and c.is_archive=false""";
        List<ColumnResponse>columnResponses=jdbcTemplate.query(sql,((rs, rowNum) -> new ColumnResponse(rs.getLong("id"),
                rs.getString("title"),rs.getBoolean("is_archive"))),boardId);
        return columnResponses.stream().map(x -> new ColumnResponse(x.getColumnId(), x.getTitle(),x.getIsArchive())).toList();
    }
}

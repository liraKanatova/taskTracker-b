package peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.jdbcTemplateImpl;

import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.repositories.jdbcTemplateService.WorkSpaceJdbcTemplateService;


@Repository
public class WorkSpaceJdbcTemplateServiceImpl implements WorkSpaceJdbcTemplateService {

    public String getAllWorkSpaces() {
        String sql = "SELECT w.id AS id, w.name AS workSpaceName," +
                "  u.id AS userId," +
                "  CONCAT(u.first_name, ' ', u.last_name) AS fullName" +
                "  ,u.image as image" +
                "  FROM work_spaces AS w" +
                " JOIN users AS u ON w.admin_id = u.id" +
                "  WHERE u.id = ?";
        return sql;
    }

    @Override
    public String getAllWorkSpaceInnerPage() {
        String sql = "SELECT w.id AS id, w.name AS workSpaceName, " +
                "u.id AS userId, b.id AS boardId, b.title AS title, b.back_ground AS back_ground, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS fullName, u.image AS image " +
                "FROM work_spaces AS w " +
                "JOIN users AS u ON w.admin_id = u.id " +
                "JOIN boards b ON w.id = b.work_space_id " +
                "WHERE u.id = ? AND w.id = ?";
        return sql;
    }


}


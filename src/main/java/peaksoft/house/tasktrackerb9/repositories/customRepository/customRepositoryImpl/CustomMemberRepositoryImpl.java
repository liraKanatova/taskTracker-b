package peaksoft.house.tasktrackerb9.repositories.customRepository.customRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.house.tasktrackerb9.dto.response.AllMemberResponse;
import peaksoft.house.tasktrackerb9.dto.response.MemberResponse;
import peaksoft.house.tasktrackerb9.enums.Role;
import peaksoft.house.tasktrackerb9.repositories.customRepository.CustomMemberRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public AllMemberResponse getAll(Long cardId) {
        String sql = """
                SELECT
                    u.id AS member_id,
                    u.first_name AS first_name,
                    u.last_name AS last_name,
                    u.email AS email,
                    u.image AS image,
                    u.role AS role
                    FROM users u JOIN boards_members bu ON u.id = bu.members_id
                    LEFT  JOIN cards_members cu ON u.id = cu.members_id
                    WHERE cu.cards_id = ?;
                               
                 """;
        List<MemberResponse> boardMembers = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new MemberResponse(
                        rs.getLong("member_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("image"),
                        Role.valueOf(rs.getString("role")))
                , cardId);
        String sql1 = """
                 SELECT
                         u.id AS memberId,
                         u.first_name AS first_name,
                         u.last_name AS last_name,
                         u.email AS email,
                         u.role AS role,
                         u.image AS image
                         FROM users u JOIN users_work_spaces wsu ON u.id = wsu.members_id
                         JOIN cards_members cu ON u.id = cu.members_id
                         WHERE cu.cards_id = ?
                """;
        List<MemberResponse> workSpaceMembers = jdbcTemplate.query(
                sql1,
                (rs, rowNum) -> new MemberResponse(
                        rs.getLong("memberId"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("image"),
                        Role.valueOf(rs.getString("role")))
                , cardId);
        return AllMemberResponse
                .builder()
                .boardMembers(boardMembers)
                .workSpaceMembers(workSpaceMembers)
                .build();
    }

    @Override
    public List<MemberResponse> searchByEmail(Long workSpaceId, String email) {
        String sql = """
                SELECT u.*
                        FROM users u
                        JOIN users_work_spaces uwsr ON uwsr.work_spaces_id = ?
                        AND uwsr.members_id = u.id
                        WHERE  lower(concat(u.first_name, u.last_name, u.email)) LIKE lower(concat('%',?,'%'));
                  """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MemberResponse(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("image"),
                        Role.valueOf(rs.getString("role"))),
                workSpaceId,
                email
        );
    }

    @Override
    public List<MemberResponse> getAllMembersFromBoard(Long boardId) {
        String sql = """
                SELECT
                    u.id AS member_id,
                    u.first_name AS first_name,
                    u.last_name AS last_name,
                    u.email AS email,
                    u.image AS image,
                    u.role AS role
                    FROM users u JOIN boards_members bu ON u.id = bu.members_id
                    WHERE bu.boards_id = ?;
                 """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new MemberResponse(
                                rs.getLong("member_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("image"),
                                Role.valueOf(rs.getString("role")))
                , boardId);
    }
}
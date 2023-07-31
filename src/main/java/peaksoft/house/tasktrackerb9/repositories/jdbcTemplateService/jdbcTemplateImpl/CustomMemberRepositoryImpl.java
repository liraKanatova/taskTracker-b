package peaksoft.tasktracker.repository.custom.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import peaksoft.tasktracker.dto.response.AllMemberResponse;
import peaksoft.tasktracker.dto.response.MemberResponse;
import peaksoft.tasktracker.enums.Role;
import peaksoft.tasktracker.repository.custom.CustomMemberRepository;

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
                       u.id AS user_id,
                       u.first_name as first_name,
                       u.last_name as last_name,
                       u.email AS email,
                       u.image as image,
                       u.role as role
                       FROM users u join boards_members bu on u.id = bu.members_id
                       left  join cards_members cu on u.id = cu.members_id
                       WHERE cu.cards_id = ?;
               
                """;

        List<MemberResponse> boardMembers = jdbcTemplate.query(
                sql,
                (rs,rowNum) -> new MemberResponse(
                        rs.getLong("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("image"),
                        Role.valueOf(rs.getString("role")))
                ,cardId);

        String sql1 = """
                 SELECT
                         u.id AS userId,
                         u.first_name as first_name,
                         u.last_name as last_name,
                         u.email AS email,
                         u.role as role,
                         u.image as image
                 FROM users u join work_spaces_members wsu on u.id = wsu.members_id
                         join cards_members cu on u.id = cu.members_id
                         where cu.cards_id = ?
                """;


        List<MemberResponse> workSpaceMembers = jdbcTemplate.query(
                sql1,
                (rs,rowNum) -> new MemberResponse(
                        rs.getLong("userId"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("image"),
                        Role.valueOf(rs.getString("role")))
                ,cardId);

        return AllMemberResponse
                .builder()
                .boardMembers(boardMembers)
                .workSpaceMembers(workSpaceMembers)
                .build();

}
}

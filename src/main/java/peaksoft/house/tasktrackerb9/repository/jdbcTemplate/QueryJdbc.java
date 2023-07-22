package peaksoft.house.tasktrackerb9.repository.jdbcTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Service
public class QueryJdbc {

    private String updateUser ="UPDATE users AS u SET u.first_name=?,u.last_name=?,u.email=?,u.password=? where u.id=:id";

    private String getById="select u.id,u.first_name,u.last_name,u.email,u.password,u.image from users u where u=?";
}

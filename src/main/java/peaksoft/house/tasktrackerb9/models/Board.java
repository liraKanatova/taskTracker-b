package peaksoft.house.tasktrackerb9.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(generator = "board_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "board_gen",sequenceName = "board_seq",allocationSize = 1)
    private Long id;
    private String title;

    @jakarta.persistence.Column(name = "back_ground")
    private String backGround;

    @OneToOne(cascade = {ALL},mappedBy = "board")
    private Favorite favorite;

    @ManyToOne(cascade = {DETACH,MERGE,REFRESH})
    private WorkSpace workSpace;

    @ManyToMany(cascade = {DETACH,MERGE,REFRESH})
    private List<User>users;

    @OneToMany(cascade = ALL,mappedBy = "board")
    private List<Column>columns;

}
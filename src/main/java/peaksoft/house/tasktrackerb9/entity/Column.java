package peaksoft.house.tasktrackerb9.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "columns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Column {

    @Id
    @GeneratedValue(generator = "column_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "column_gen", sequenceName = "column_seq", allocationSize = 1)
    private Long id;

    private String title;

    @jakarta.persistence.Column(name = "is_archive")
    private Boolean isArchive;

    @ManyToMany(cascade = {DETACH, MERGE, REFRESH})
    private List<User> users;

    @OneToMany(cascade = {ALL},mappedBy = "column")
    private List<Card> cards;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH})
    private Board board;
}

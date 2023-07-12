package peaksoft.house.tasktrackerb9.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.*;
@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {
    @Id
    @GeneratedValue(generator = "favorite_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "favorite_gen", sequenceName = "favorite_seq", allocationSize = 1)
    private Long id;
    @OneToOne(cascade = {DETACH,MERGE,REFRESH})
    private Board board;
    @OneToOne(cascade = {DETACH,MERGE,REFRESH})
    private WorkSpace workSpace;
    @ManyToOne(cascade = {DETACH,MERGE,REFRESH})
    private User user;

}

package peaksoft.house.tasktrackerb9.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "checkLists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckList {

    @Id
    @GeneratedValue(generator = "checkList_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "checkList_gen", sequenceName = "checkList_seq", allocationSize = 1)
    private Long id;

    private String description;

    private int percent;

    @OneToMany(cascade = {MERGE,REFRESH,DETACH},mappedBy = "checkList")
    private List<Item>items;

    @ManyToOne(cascade = {MERGE,REFRESH,DETACH})
    private Card card;
}

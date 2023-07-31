package peaksoft.house.tasktrackerb9.models;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {

    @Id
    @GeneratedValue(generator = "label_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "label_gen", sequenceName = "label_seq", allocationSize = 1,initialValue = 6)
    private Long id;

    @Column(name = "label_name")
    private String labelName;

    private String color;

    @ManyToMany(cascade = {DETACH, MERGE, REFRESH})
    @JoinTable(name = "labels_cards",
            joinColumns = @JoinColumn(name = "labels_id"),
            inverseJoinColumns = @JoinColumn(name = "cards_id"))
    private List<Card> cards;

    public Label(String labelName, String color) {
        this.labelName = labelName;
        this.color = color;
    }
}

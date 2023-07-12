package peaksoft.house.tasktrackerb9.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(generator = "card_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "card_gen",sequenceName = "card_seq",allocationSize = 1)
    private Long id;
    private String title;
    private String description;
    @jakarta.persistence.Column(name = "is_archive")
    private Boolean isArchive;
    @ManyToMany(cascade ={DETACH,MERGE,REFRESH})
    private List<User>users;
    @ManyToMany(cascade = {DETACH,MERGE,REFRESH},mappedBy = "cards")
    private List<Label>labels;
    @OneToMany(cascade = {DETACH,MERGE,REFRESH},mappedBy = "card")
    private List<Notification>notifications;
    @OneToMany(cascade = {ALL},mappedBy = "card")
    private List<Attachment>attachments;
    @OneToMany(cascade = {ALL},mappedBy = "card")
    private List<Comment>comments;
    @OneToMany(cascade = {ALL},mappedBy = "card")
    private List<CheckList>checkLists;
    @OneToOne(cascade = {ALL},mappedBy = "card")
    private Estimation estimation;
    @ManyToOne(cascade = {DETACH,MERGE,REFRESH})
    private Column column;
}

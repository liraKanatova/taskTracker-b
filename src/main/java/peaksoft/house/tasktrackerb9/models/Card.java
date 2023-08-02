package peaksoft.house.tasktrackerb9.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    @SequenceGenerator(name = "card_gen",sequenceName = "card_seq",allocationSize = 1,
    initialValue = 6)
    private Long id;

    private String title;

    private String description;

    @jakarta.persistence.Column(name = "is_archive")
    private Boolean isArchive;

    private ZonedDateTime createdDate;

    @ManyToMany(cascade ={DETACH,MERGE,REFRESH})
    private List<User>members;

    @ManyToMany(cascade = {DETACH,MERGE,REFRESH},mappedBy = "cards")
    private List<Label>labels;

    @OneToMany(cascade = {DETACH,MERGE,REFRESH},mappedBy = "card")
    private List<Notification>notifications;

    @OneToMany(cascade = {ALL},mappedBy = "card")
    private List<Attachment>attachments;

    @OneToMany(cascade = {ALL},mappedBy = "card")
    private List<Comment>comments;

    @OneToMany(cascade = {ALL},mappedBy = "card",orphanRemoval = true)
    private List<CheckList>checkLists;

    @OneToOne(cascade = {ALL},mappedBy = "card")
    private Estimation estimation;

    @ManyToOne(cascade = {DETACH,MERGE,REFRESH})
    private Column column;

    private Long creatorId;

    public Card(String title,String description) {
        this.title = title;
        this.description = description;
    }

    public void addChecklist(CheckList checklist) {
        if (checkLists == null) {
            checkLists = new ArrayList<>();
        }
        checkLists.add(checklist);
    }

    public void addMember(User user) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(user);
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    public void addLabel(Label label) {
        if (labels == null) {
            labels = new ArrayList<>();
        }
        labels.add(label);
    }
}
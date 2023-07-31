package peaksoft.house.tasktrackerb9.models;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.*;
import peaksoft.house.tasktrackerb9.enums.NotificationType;

import java.time.ZonedDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(generator = "notification_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "notification_gen",sequenceName = "notification_seq",allocationSize = 1,initialValue = 6)
    private Long id;

    private String text;

    private String image;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "is_read")
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToMany(cascade = {DETACH,MERGE,REFRESH})
    private List<User> members;

    @ManyToOne(cascade = {DETACH,MERGE,REFRESH})
    private Card card;

    @OneToOne(cascade = {DETACH,MERGE,REFRESH,REMOVE},mappedBy = "notification")
    private Estimation estimation;
}

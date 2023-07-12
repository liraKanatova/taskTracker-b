package peaksoft.house.tasktrackerb9.entity;


import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.*;

import java.time.ZonedDateTime;

import static jakarta.persistence.CascadeType.*;
@Entity
@Table(name = "estimations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estimation {

    @Id
    @GeneratedValue(generator = "estimation_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "estimation_gen", sequenceName = "estimation_seq", allocationSize = 1)
    private Long id;
    private String reminder;
    @Column(name = "start_date")
    private ZonedDateTime startDate;
    @Column(name = "due_date")
    private ZonedDateTime duetDate;
    private ZonedDateTime time;
    @OneToOne(cascade = {DETACH,MERGE,REFRESH})
    private Card card;
    @OneToOne(cascade = {DETACH,MERGE,REFRESH})
    private Notification notification;
}

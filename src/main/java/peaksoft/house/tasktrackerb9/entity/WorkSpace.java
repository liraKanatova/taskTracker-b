package peaksoft.house.tasktrackerb9.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "work_spaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSpace {

    @Id
    @GeneratedValue(generator = "workSpace_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "workSpace_gen",sequenceName = "workSpace_seq",allocationSize = 1)
    private Long id;

    private String name;

    @Column(name = "admin_id")
    private Long adminId;

    @ManyToMany(mappedBy = "workSpaces",cascade ={DETACH,REFRESH,MERGE})
    private List<User>users;

    @OneToMany(cascade = {ALL},mappedBy = "workSpace")
    private List<Board>boards;

    @OneToOne(cascade = {ALL})
    private Favorite favorite;

    @OneToMany(cascade = {ALL},mappedBy = "workSpace")
    private List<UserWorkSpaceRole>roles;



}

package peaksoft.house.tasktrackerb9.entity;

import jakarta.persistence.*;
import lombok.*;
import peaksoft.house.tasktrackerb9.enums.Role;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "user_work_space_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWorkSpaceRole {

    @Id
    @GeneratedValue(generator = "userWorkSpaceRole_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "userWorkSpaceRole_gen", sequenceName = "userWorkSpaceRole_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH})
    private User user;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH})
    private WorkSpace workSpace;


}

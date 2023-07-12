package peaksoft.house.tasktrackerb9.entity;

import jakarta.persistence.*;
import lombok.*;
import peaksoft.house.tasktrackerb9.enums.Role;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(generator = "users_gen",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_gen",sequenceName = "users_seq",allocationSize = 1)
    private Long id;
    @jakarta.persistence.Column(name = "first_name")
    private String firstName;
    @jakarta.persistence.Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    private String image;
    @ManyToMany(cascade = {MERGE,DETACH,REFRESH})
    private List<WorkSpace> spaces;
    @OneToMany(cascade = {ALL}, mappedBy = "user")
    private List<Favorite> favorites;
    @ManyToMany(cascade = {MERGE,DETACH,REFRESH}, mappedBy = "users")
    private List<Column> columns;
    @ManyToMany(cascade = {MERGE,DETACH,REFRESH}, mappedBy = "users")
    private List<Card> cards;
    @ManyToMany(cascade = {MERGE,DETACH,REFRESH}, mappedBy = "users")
    private List<Notification> notifications;
    @OneToMany(cascade = {ALL}, mappedBy = "user")
    private List<Comment> comments;
    @ManyToMany(cascade = {MERGE,DETACH,REFRESH}, mappedBy = "users")
    private List<Board> boards;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(cascade = {ALL}, mappedBy = "user")
    private List<UserWorkSpaceRole> roles;

}

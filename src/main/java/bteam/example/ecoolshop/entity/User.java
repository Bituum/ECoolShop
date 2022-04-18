package bteam.example.ecoolshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "custom_user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "username")
    @NotNull(message = "username must not be null")
    private String username;

    @Column(name = "email")
    @NotNull(message = "email must not be null")
    private String email;

    @Column(name = "user_password")
    @NotNull(message = "password must not be null")
    private char[] password;

    @Column(name = "birthday")
    @NotNull(message = "birthday must not be null")
    private LocalDateTime birthday;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @NotNull(message = "role must not be null")
    private Set<Role> userRole;
}

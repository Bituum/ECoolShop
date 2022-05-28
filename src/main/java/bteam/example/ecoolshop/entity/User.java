package bteam.example.ecoolshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "custom_user", schema = "public")
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime birthday;

    @Column(name = "photo_path")
    private String photoPath;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @NotNull(message = "role must not be null")
    @JsonIgnore
    private Set<Role> userRole;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public User(String username, String email, LocalDateTime birthday, char[] password) {
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
    }
}

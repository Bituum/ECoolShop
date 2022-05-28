package bteam.example.ecoolshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Table(name = "db_roles", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int role_id;

    @Column(name = "role")
    private String role;

    @ManyToMany(mappedBy = "userRole")
    private List<AUser> AUsers;

    @Override
    public String getAuthority() {
        return role;
    }
}

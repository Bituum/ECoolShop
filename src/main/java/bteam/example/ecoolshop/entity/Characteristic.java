package bteam.example.ecoolshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "characteristic")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "characteristic_id")
    private int characteristic_id;

    @Column(name = "characteristic")
    private String characteristic;
}

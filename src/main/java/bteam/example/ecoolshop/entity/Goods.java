package bteam.example.ecoolshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "goods")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private int goods_id;

    @Column(name = "price")
    private int price;

    @Column(name = "photo_path")
    private String photo_path;
}

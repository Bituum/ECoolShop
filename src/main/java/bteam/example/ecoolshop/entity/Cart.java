package bteam.example.ecoolshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart", schema = "public")
public class Cart {
    @Id
    @Column(name = "cart_id")
    private int id;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "count_of_goods")
    private int countOfGoods;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_goods",
            joinColumns = {@JoinColumn(name = "cart_id")},
            inverseJoinColumns = {@JoinColumn(name = "goods_id")})
    private List<Goods> goodsList;

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    private AUser AUser;
}

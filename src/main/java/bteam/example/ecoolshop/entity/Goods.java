package bteam.example.ecoolshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Table(name = "goods", schema = "public")
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

    @Column(name = "nomination")
    @Size(max = 255, message = "size of nomination field must not be bigger than 255 characters")
    @NotNull(message = "Nomination must not be null!")
    private String nomination;

    @NotNull(message = "price must not be null!")
    @Column(name = "price")
    private int price;

    @Column(name = "photo_path")
    private String photo_path;

    @ManyToMany(mappedBy = "goodsList")
    @JsonIgnore
    private List<Cart> cartList;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "description_id")
    private Description description;
}

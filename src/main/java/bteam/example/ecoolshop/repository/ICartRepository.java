package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<Cart, Integer> {

    @Query(value =
            "select cart_id" +
                    " from cart as c" +
                    " join custom_user as u" +
                    " on c.cart_id = u.user_id" +
                    " where u.username = :username",
            nativeQuery = true
    )
    Optional<Integer> getCartIdByUsername(@Param("username") String username);

    @Query(value =
            "select count(cart_goods_id) " +
                    "from cart_goods as cg" +
                    " where cg.cart_id = :id",
            nativeQuery = true
    )
    Optional<Integer> getCountOfGoodsInCart(@Param("id") int id);

    @Query(value =
            "select sum(price) from cart" +
                    " join cart_goods cg on cart.cart_id = cg.cart_id" +
                    " join goods g on g.goods_id = cg.goods_id" +
                    " where cg.cart_id = :id",
            nativeQuery = true
    )
    Optional<Integer> getSumOfGoodsInCart(@Param("id") int id);

    @Modifying
    @Query(value = "delete from cart_goods as cg where cg.cart_id = :cart_id",
            nativeQuery = true
    )
    void manyToManyCascadeDelete(@Param("cart_id") int id);

}

package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.entity.Cart;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.repository.ICartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private ICartRepository cartRepository;

    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    public Cart getCartById(int id) {
        return cartRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("wrong cart id is provided")
                );
    }

    public int getCartIdByUsername(String username) {
        return cartRepository
                .getCartIdByUsername(username)
                .orElseThrow(
                        UserNotFoundException::new
                );
    }

    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void createCart(Cart cart) {
        try {
            int totalPrice = cartRepository
                    .getSumOfGoodsInCart(cart.getId())
                    .orElseThrow(
                            () -> new RuntimeException("error while extraction totalPrice of goods")
                    );
            int countOfGoods = cartRepository
                    .getCountOfGoodsInCart(cart.getId())
                    .orElseThrow(
                            () -> new RuntimeException("error while extraction count of goods")
                    );

            cart.setCountOfGoods(countOfGoods);
            cart.setTotalPrice(totalPrice);

        } catch (RuntimeException exception) {
            cartRepository.save(cart);
        }

        cartRepository.save(cart);
    }
}

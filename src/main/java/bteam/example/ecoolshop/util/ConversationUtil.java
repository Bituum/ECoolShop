package bteam.example.ecoolshop.util;

import bteam.example.ecoolshop.dto.CartDto;
import bteam.example.ecoolshop.dto.GoodsDto;
import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.Cart;
import bteam.example.ecoolshop.entity.Goods;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.service.CartService;
import bteam.example.ecoolshop.service.GoodsService;
import bteam.example.ecoolshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Scope("prototype")
public class ConversationUtil<I, O> {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final GoodsService goodsService;
    private final CartService cartService;

    public ConversationUtil(ModelMapper modelMapper, UserService userService, GoodsService goodsService, CartService cartService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.goodsService = goodsService;
        this.cartService = cartService;
    }


    public O convertToDto(I object, Class<O> c) {
        if (object instanceof User) {
            return c.cast(modelMapper.map(object, UserDto.class));
        }
        if (object instanceof Goods) {
            return c.cast(modelMapper.map(object, GoodsDto.class));
        }
        if (object instanceof Cart) {
            return c.cast(modelMapper.map(object, CartDto.class));
        }

        throw new IllegalArgumentException("Illegal type of object are provided");
    }

    public List<O> convertToList(List<I> objectList, Class<O> c) {
        return objectList.stream()
                .map(it -> convertToDto(it, c))
                .collect(Collectors.toList());
    }

    // https://rmannibucau.wordpress.com/2014/04/07/dto-to-domain-converter-with-java-8-and-cdi/
    // https://stackoverflow.com/questions/17413788/generic-dto-converter-pattern
    public O createOrConvert(I object, Class<O> c) {
        if (object instanceof UserDto) {
            User user = modelMapper.map(object, User.class);

            try {
                user = userService.getUserById(userService.getIdByUsername(((UserDto) object).getUsername()));
            } catch (UserNotFoundException e) {
                userService.createUser(user);
            }

            return c.cast(user);
        }
        if (object instanceof GoodsDto) {
            Goods goods = modelMapper.map(object, Goods.class);

            try {
                goods = goodsService.getGoodsById(goodsService.getIdByNomination(((GoodsDto) object).getNomination()));
            } catch (IllegalArgumentException argumentException) {
                goodsService.createGoods(goods);
            }

            return c.cast(goods);
        }
        if (object instanceof CartDto) {
            Cart cart = modelMapper.map(object, Cart.class);

            try {
                cart = cartService.getCartById(cartService.getCartIdByUsername(((CartDto) object).getUsername()));
            } catch (IllegalArgumentException argumentException) {
                cart.setGoodsList(((CartDto) object).getGoodsList());
                cartService.createCart(cart);
            }
            cart.setGoodsList(((CartDto) object).getGoodsList());

            return c.cast(cart);
        }
        throw new IllegalArgumentException("provided illegal type of object for conversation");
    }

}

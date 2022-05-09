package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.CartDto;
import bteam.example.ecoolshop.entity.Cart;
import bteam.example.ecoolshop.service.CartService;
import bteam.example.ecoolshop.util.ConversationUtil;
import bteam.example.ecoolshop.util.MapResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bteam.example.ecoolshop.util.ExceptionMapBuilder.getStringStringMap;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final ConversationUtil<Cart, CartDto> conversationToDtoUtil;
    private final ConversationUtil<CartDto, Cart> conversationToCartUtil;

    public CartController(ConversationUtil<Cart, CartDto> conversationToDtoUtil, ConversationUtil<CartDto, Cart> conversationToCartUtil, CartService cartService) {
        this.conversationToDtoUtil = conversationToDtoUtil;
        this.conversationToCartUtil = conversationToCartUtil;
        this.cartService = cartService;
    }

    @PostMapping("/")
    public ResponseEntity<Map<Object, Object>> addGoodsToProvidedUser(@Valid @RequestBody CartDto cartDto) {
        cartService.createCart(
                conversationToCartUtil.createOrConvert(cartDto, Cart.class)
        );

        return ResponseEntity.ok(MapResponse.OkResponse("create cart of user: ", cartDto.getUsername()));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Map<Object, Object>> deleteCartOfUser(@PathVariable("username") String username) {
        cartService.deleteCart(
                cartService.getCartIdByUsername(username)
        );

        return ResponseEntity.ok(MapResponse.OkResponse("Deleted cart of user:", username));
    }

    @GetMapping("/all")
    public List<CartDto> showAllCarts() {
        return cartService.getAll()
                .stream()
                .map(user -> conversationToDtoUtil.convertToDto(user, CartDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{username}")
    public CartDto showCartOfProvidedUser(@PathVariable("username") String username) {
        return conversationToDtoUtil.convertToDto(
                cartService.getCartById(cartService.getCartIdByUsername(username)),
                CartDto.class
        );
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return getStringStringMap(ex);
    }
}

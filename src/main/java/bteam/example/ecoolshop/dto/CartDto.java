package bteam.example.ecoolshop.dto;

import bteam.example.ecoolshop.entity.Goods;
import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private String username;
    private int totalPrice;
    private int countOfGoods;
    List<Goods> goodsList;
}

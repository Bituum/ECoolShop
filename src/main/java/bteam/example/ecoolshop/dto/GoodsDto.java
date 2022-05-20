package bteam.example.ecoolshop.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GoodsDto {
    @Size(max = 255, message = "size of nomination field must not be bigger than 255 characters")
    @NotNull(message = "Nomination must not be null!")
    private String nomination;
    @NotNull(message = "price must not be null!")
    private int price;
}

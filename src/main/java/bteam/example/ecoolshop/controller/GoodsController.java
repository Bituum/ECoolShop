package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.GoodsDto;
import bteam.example.ecoolshop.entity.Goods;
import bteam.example.ecoolshop.service.GoodsService;
import bteam.example.ecoolshop.util.ConversationUtil;
import bteam.example.ecoolshop.util.MapResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static bteam.example.ecoolshop.util.ExceptionMapBuilder.getStringStringMap;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    private final GoodsService goodsService;
    private final ConversationUtil<Goods, GoodsDto> conversationToDtoUtil;
    private final ConversationUtil<GoodsDto, Goods> conversationToGoodsUtil;

    public GoodsController(GoodsService goodsService, ConversationUtil<Goods, GoodsDto> conversationToDtoUtil, ConversationUtil<GoodsDto, Goods> conversationToGoodsUtil) {
        this.goodsService = goodsService;
        this.conversationToDtoUtil = conversationToDtoUtil;
        this.conversationToGoodsUtil = conversationToGoodsUtil;
    }

    @PostMapping("/")
    public ResponseEntity<Map<Object, Object>> addNewGoods(@Valid @RequestBody GoodsDto goodsDto) {
        goodsService.createGoods(
                conversationToGoodsUtil.createOrConvert(goodsDto, Goods.class)
        );

        return ResponseEntity.ok(MapResponse.OkResponse("goods been added", goodsDto.getNomination()));
    }

    @PostMapping("/description")
    public ResponseEntity<Map<Object, Object>> addDescription(@Valid @RequestBody GoodsDto goodsDto) {
        goodsService.createGoods(
                conversationToGoodsUtil.createOrConvert(goodsDto, Goods.class)
        );

        return ResponseEntity.ok(MapResponse.OkResponse("goods been added", goodsDto.getNomination()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return getStringStringMap(ex);
    }
}

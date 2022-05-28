package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.GoodsDto;
import bteam.example.ecoolshop.entity.Goods;
import bteam.example.ecoolshop.service.GoodsService;
import bteam.example.ecoolshop.util.ConversationUtil;
import bteam.example.ecoolshop.util.enums.Order;
import bteam.example.ecoolshop.util.maps.ResponseMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

        return ResponseEntity.ok(ResponseMap.OkResponse("goods been added", goodsDto.getNomination()));
    }

    @PostMapping("/description")
    public ResponseEntity<Map<Object, Object>> addDescription(@Valid @RequestBody GoodsDto goodsDto) {
        goodsService.createGoods(
                conversationToGoodsUtil.createOrConvert(goodsDto, Goods.class)
        );

        return ResponseEntity.ok(ResponseMap.OkResponse("goods been added", goodsDto.getNomination()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return getStringStringMap(ex);
    }

    /*
     *   Example of the request
     *   localhost:8080/api/goods/filter?key=price&order=asc
     */
    @GetMapping("/sort")
    public List<GoodsDto> getFilteredResult(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "key") String key) {
        Page<Goods> filteredGoods;
        if (order.equals("asc")) {
            filteredGoods = goodsService.getFilterGoods(key, page, size, Sort.Direction.ASC);
        } else {
            //desc
            filteredGoods = goodsService.getFilterGoods(key, page, size, Sort.Direction.DESC);
        }

        return filteredGoods.getContent().stream()
                .map(it -> conversationToDtoUtil.convertToDto(it, GoodsDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("")
    public List<GoodsDto> getGoods(
            @RequestParam(name = "order", required = false, defaultValue = "false") boolean order,
            @RequestParam(name = "price", defaultValue = "0") int price
    ) {
        if (order) {
            List<Goods> biggerList = goodsService.getGoodsLessOrBiggerThan(price, Order.GREATER);

            return conversationToDtoUtil.convertToList(biggerList, GoodsDto.class);
        } else {
            List<Goods> lesserList = goodsService.getGoodsLessOrBiggerThan(price, Order.GREATER);

            return conversationToDtoUtil.convertToList(lesserList, GoodsDto.class);
        }
    }
}

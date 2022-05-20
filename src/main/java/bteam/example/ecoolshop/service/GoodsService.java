package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.entity.Description;
import bteam.example.ecoolshop.entity.Goods;
import bteam.example.ecoolshop.repository.DescriptionRepository;
import bteam.example.ecoolshop.repository.GoodsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final DescriptionRepository descriptionRepository;

    public GoodsService(GoodsRepository goodsRepository, DescriptionRepository descriptionRepository) {
        this.goodsRepository = goodsRepository;
        this.descriptionRepository = descriptionRepository;
    }

    public void createGoods(Goods goods) {
        Description description = new Description();
        goods.setDescription(description);

        goodsRepository.save(goods);
    }

    @Transactional
    public void deleteGoods(String nomination) {
        descriptionRepository
                .deleteById(
                        goodsRepository.findById(
                                        goodsRepository.getIdByNomination(nomination)
                                                .orElseThrow(
                                                        () -> new IllegalArgumentException("wrong nomination id is provided")
                                                )
                                ).orElseThrow(
                                        () -> new IllegalArgumentException("wrong goods is is provided")
                                )
                                .getDescription()
                                .getDescription_id()
                );

        goodsRepository.deleteByNomination(nomination);
    }

    public Goods getGoodsById(int id) {
        return goodsRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("wrong goods id is provided")
                );
    }

    public int getIdByNomination(String nomination) {
        return goodsRepository.getIdByNomination(nomination)
                .orElseThrow(
                        () -> new IllegalArgumentException("wrong nomination is provided")
                );
    }

    public Page<Goods> getFilterGoods(String key, int page, int size, Sort.Direction direction) {
//        Sort sort = Sort.by(Sort.Direction.DESC, key);
//        PageRequest page = PageRequest.of(offset, base, sort);
//        System.out.println("coool");
//        return goodsRepository.findAll(page);
        Pageable sortedByPriceDesc =
                PageRequest.of(page, size, Sort.by(direction, key));
        return goodsRepository.findAll(sortedByPriceDesc);
    }
}

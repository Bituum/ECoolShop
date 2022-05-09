package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.entity.Description;
import bteam.example.ecoolshop.entity.Goods;
import bteam.example.ecoolshop.repository.IDescriptionRepository;
import bteam.example.ecoolshop.repository.IGoodsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GoodsService {
    private final IGoodsRepository goodsRepository;
    private final IDescriptionRepository descriptionRepository;

    public GoodsService(IGoodsRepository goodsRepository, IDescriptionRepository descriptionRepository) {
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
                .deleteDescriptionByDescription_id(
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
}
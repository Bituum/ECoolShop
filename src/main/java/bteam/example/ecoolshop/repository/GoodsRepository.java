package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GoodsRepository extends CrudRepository<Goods, Integer> {
    Optional<Integer> getIdByNomination(String nomination);

    Page<Goods> findAll(Pageable pageable);

    void deleteByNomination(String nomination);
}

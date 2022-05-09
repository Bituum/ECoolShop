package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IGoodsRepository extends JpaRepository<Goods, Integer> {
    Optional<Integer> getIdByNomination(String nomination);

    void deleteByNomination(String nomination);
}

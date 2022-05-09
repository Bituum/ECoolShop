package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDescriptionRepository extends JpaRepository<Description, Integer> {
    void deleteDescriptionByDescription_id(int id);
}

package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionRepository extends JpaRepository<Description, Integer> {
}

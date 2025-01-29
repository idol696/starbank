package com.skypro.starbank.repository;

import com.skypro.starbank.model.rules.RuleSet;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Cacheable(value = "rules", key = "#productId")
public interface RuleSetRepository extends JpaRepository<RuleSet, Long> {
    Optional<RuleSet> findByProductId(UUID productId);
}


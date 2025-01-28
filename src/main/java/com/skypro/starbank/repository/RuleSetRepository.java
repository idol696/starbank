package com.skypro.starbank.repository;

import com.skypro.starbank.model.rules.RuleSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleSetRepository extends JpaRepository<RuleSet, UUID> {
    Optional<RuleSet> findByProductId(UUID productId);
}


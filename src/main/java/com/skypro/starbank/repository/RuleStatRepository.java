package com.skypro.starbank.repository;

import com.skypro.starbank.model.RuleStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleStatRepository extends JpaRepository<RuleStat, UUID> {
    Optional<RuleStat> findByRuleId(Long ruleId);
    void deleteByRuleId(Long ruleId);
}

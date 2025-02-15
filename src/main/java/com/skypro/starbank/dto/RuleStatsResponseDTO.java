package com.skypro.starbank.dto;

import com.skypro.starbank.model.RuleStat;
import java.util.List;

public record RuleStatsResponseDTO(List<RuleStat> stats) {}

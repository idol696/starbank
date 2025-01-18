package com.skypro.starbank.controller;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.service.RuleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rules")
public class RuleController {
    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    public List<RuleSet> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/{productId}")
    public RuleSet getRulesByProductId(@PathVariable String productId) {
        return ruleService.getRulesByProductId(productId);
    }

    @PostMapping("/set")
    public String setRules(@RequestBody List<RuleSet> rules) {
        ruleService.setRules(rules);
        return "✅ Правила обновлены и сохранены в файл.";
    }

    @PutMapping("/{productId}")
    public String updateRulesForProduct(@PathVariable String productId, @RequestBody List<Rule> newConditions) {
        ruleService.updateRulesForProduct(productId, newConditions);
        return "✅ Правила для продукта " + productId + " обновлены.";
    }

    @PostMapping("/save")
    public String saveRules() {
        ruleService.saveRulesAsync();
        return "⏳ Сохранение правил запущено в фоновом режиме.";
    }
}


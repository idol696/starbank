package com.skypro.starbank.controller;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.model.rules.RuleSetWrapper;
import com.skypro.starbank.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для управления наборами правил (RuleSet).
 */
@RestController
@RequestMapping("/rule")
@Tag(name = "Rule Controller", description = "API для управления наборами правил.")
public class RuleController {

    private final RuleService ruleService;

    /**
     * Конструктор для RuleController.
     *
     * @param ruleService Сервис для работы с наборами правил.
     */
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * Создает новый набор правил.
     *
     * @param ruleSet Набор правил для создания.
     * @return {@link ResponseEntity} с созданным набором правил.
     */
    @PostMapping
    @Operation(summary = "Создать новый набор правил.", description = "Создает новый набор правил и возвращает его.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Набор правил успешно создан",
                    content = @Content(schema = @Schema(implementation = RuleSet.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе")
    })
    public ResponseEntity<RuleSet> createRule(
            @Parameter(description = "Набор правил для создания", required = true)
            @RequestBody RuleSet ruleSet) {
        ruleService.setRules(ruleSet);
        return ResponseEntity.ok(ruleSet);
    }

    /**
     * Получает все наборы правил.
     *
     * @return {@link ResponseEntity} со списком всех наборов правил в формате, указанном в требованиях.
     */
    @GetMapping
    @Operation(summary = "Получить все наборы правил.", description = "Возвращает список всех наборов правил.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список наборов правил успешно получен",
                    content = @Content(schema = @Schema(implementation = RuleSetWrapper.class)))
    })
    public ResponseEntity<RuleSetWrapper> getAllRules() {
        List<RuleSet> rules = ruleService.getAllRules();

        return ResponseEntity.ok(new RuleSetWrapper(rules));
    }

    /**
     * Получает набор правил по ID продукта.
     *
     * @param productId ID продукта.
     * @return {@link ResponseEntity} с набором правил для указанного продукта.
     */
    @GetMapping("/product/{productId}")
    @Operation(summary = "Получить набор правил по ID продукта.", description = "Возвращает набор правил для указанного продукта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Набор правил успешно получен",
                    content = @Content(schema = @Schema(implementation = RuleSet.class))),
            @ApiResponse(responseCode = "404", description = "Набор правил для указанного продукта не найден")
    })
    public ResponseEntity<RuleSet> getRulesByProductId(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable String productId) {
        RuleSet ruleSet = ruleService.getRulesByProductId(productId);
        if (ruleSet != null) {
            return ResponseEntity.ok(ruleSet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Обновляет набор правил для конкретного продукта.
     *
     * @param productId     ID продукта.
     * @param newConditions Новый список условий.
     * @return {@link ResponseEntity} с HTTP статусом 200 OK, если обновление прошло успешно.
     */
    @PutMapping("/product/{productId}")
    @Operation(summary = "Обновить набор правил для продукта.", description = "Обновляет набор правил для указанного продукта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Набор правил успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Продукт с указанным ID не найден")
    })
    public ResponseEntity<Void> updateRulesForProduct(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable String productId,
            @Parameter(description = "Новый список условий", required = true)
            @RequestBody RuleSet newConditions) {
        ruleService.updateRulesForProduct(productId, newConditions);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет набор правил по его ID.
     *
     * @param ruleId ID набора правил.
     * @return {@link ResponseEntity} с HTTP статусом 204 No Content.
     */
    @DeleteMapping("/{ruleId}")
    @Operation(summary = "Удалить набор правил по ID.", description = "Удаляет набор правил с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Набор правил успешно удален"),
            @ApiResponse(responseCode = "404", description = "Набор правил с указанным ID не найден")
    })
    public ResponseEntity<Void> deleteRule(
            @Parameter(description = "ID набора правил", required = true)
            @PathVariable Long ruleId) {
        //   ruleService.deleteRule(ruleId); ] /todo сделать реализацию этого метода в RuleServiceImpl
        return ResponseEntity.noContent().build();
    }
}
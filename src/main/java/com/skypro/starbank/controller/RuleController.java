package com.skypro.starbank.controller;

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

import java.util.List;

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
           ruleService.deleteRuleSet(ruleId);
        return ResponseEntity.noContent().build();
    }
}
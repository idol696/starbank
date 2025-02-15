package com.skypro.starbank.controller;

import com.skypro.starbank.dto.RuleStatsResponseDTO;
import com.skypro.starbank.model.RuleStat;
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
     * @return {@link ResponseEntity} с HTTP статусом 200 возвращает удаленный объект.
     */
    @DeleteMapping("/{ruleId}")
    @Operation(summary = "Удалить набор правил по ID.", description = "Удаляет набор правил с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Набор правил успешно удален",
                    content = @Content(schema = @Schema(implementation = RuleSet.class))),
            @ApiResponse(responseCode = "404", description = "Набор правил с указанным ID не найден")
    })
    public ResponseEntity<RuleSet> deleteRule(
            @Parameter(description = "ID набора правил", required = true)
            @PathVariable Long ruleId) {
        RuleSet ruleSet = ruleService.deleteRuleSet(ruleId);
        return ResponseEntity.ok(ruleSet);
    }

    /**
     * Возвращает статистику срабатываний всех правил рекомендаций.
     * Если правило никогда не срабатывало, оно будет присутствовать в списке со значением счетчика 0.
     *
     * @return HTTP-ответ со статистикой правил.
     */
    @GetMapping("/stats")
    @Operation(
            summary = "Получить статистику срабатывания правил",
            description = "Возвращает детальную статистику всех правил рекомендаций, включая те, которые не срабатывали."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Статистика успешно получена",
            content = @Content(schema = @Schema(implementation = RuleStatsResponseDTO.class))
    )
    public ResponseEntity<RuleStatsResponseDTO> getRuleStats() {
        List<RuleStat> stats = ruleService.getRuleStats();
        return ResponseEntity.ok(new RuleStatsResponseDTO(stats));
    }

    /**
     * Внутренний класс для представления ответа со статистикой срабатывания правил.
     */
    @Schema(description = "Ответ с статистикой срабатывания правил")
    public record RuleStatsResponse(
            @Schema(description = "Статистика правил") List<RuleStat> stats) {
        public RuleStatsResponse(List<RuleStat> stats) {
            this.stats = stats;
        }
    }
}


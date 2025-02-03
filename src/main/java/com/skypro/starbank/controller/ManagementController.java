package com.skypro.starbank.controller;

import com.skypro.starbank.service.ManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления сервисом, включая статистику срабатывания правил рекомендаций,
 * очистку кэшей и предоставление информации о сервисе.
 */
@RestController
@RequestMapping("/management")
@Tag(name = "Management Controller", description = "API для управления сервисом, включая статистику, очистку кэшей и информацию о сервисе")
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${project.version}")
    private String serviceVersion;

    /**
     * Возвращает статистику срабатываний всех правил рекомендаций.
     * Если правило никогда не срабатывало, оно будет присутствовать в списке со значением счетчика 0.
     *
     * @return ответ со статистикой срабатываний всех правил
     */
    @GetMapping("/rule/stats")
    @Operation(summary = "Получить статистику срабатывания правил", description = "Возвращает статистику срабатывания всех правил рекомендаций")
    @ApiResponse(responseCode = "200", description = "Статистика успешно получена",
            content = @Content(schema = @Schema(implementation = RuleStatisticResponse.class)))
    public ResponseEntity<RuleStatisticResponse> getRuleStatistics() {
        List<RuleStatistic> stats = managementService.getAllRuleStatistics();
        List<RuleStatisticDTO> statsDTO = stats.stream()
                .map(stat -> new RuleStatisticDTO(stat.getRuleId(), stat.getCount()))
                .collect(Collectors.toList());

        RuleStatisticResponse response = new RuleStatisticResponse(statsDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Очищает все кэши и обновляет базу данных.
     * Этот запрос не принимает тело и не возвращает данные.
     *
     * @return ответ с кодом 200 OK
     */
    @PostMapping("/clear-caches")
    @Operation(summary = "Очистить кэши", description = "Очищает все кэши и обновляет базу данных")
    @ApiResponse(responseCode = "200", description = "Кэши успешно очищены")
    public ResponseEntity<Void> clearCaches() {
        managementService.clearCaches();
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает информацию о сервисе, включая его название и версию.
     *
     * @return ответ с информацией о сервисе
     */
    @GetMapping("/info")
    @Operation(summary = "Получить информацию о сервисе", description = "Возвращает название и версию сервиса")
    @ApiResponse(responseCode = "200", description = "Информация о сервисе успешно получена",
            content = @Content(schema = @Schema(implementation = ServiceInfoResponse.class)))
    public ResponseEntity<ServiceInfoResponse> getServiceInfo() {
        ServiceInfoResponse response = new ServiceInfoResponse(serviceName, serviceVersion);
        return ResponseEntity.ok(response);
    }

    /**
     * Внутренний класс для представления ответа со статистикой срабатывания правил.
     */
    @Schema(description = "Ответ с статистикой срабатывания правил")
    public static class RuleStatisticResponse {
        private List<RuleStatisticDTO> stats;

        public RuleStatisticResponse(List<RuleStatisticDTO> stats) {
            this.stats = stats;
        }

        public List<RuleStatisticDTO> getStats() {
            return stats;
        }

        public void setStats(List<RuleStatisticDTO> stats) {
            this.stats = stats;
        }
    }

    /**
     * Внутренний класс для представления статистики срабатывания одного правила.
     */
    @Schema(description = "DTO для статистики срабатывания правила")
    public static class RuleStatisticDTO {
        private String ruleId;
        private int count;

        public RuleStatisticDTO(String ruleId, int count) {
            this.ruleId = ruleId;
            this.count = count;
        }

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    /**
     * Внутренний класс для представления информации о сервисе.
     */
    @Schema(description = "Ответ с информацией о сервисе")
    public static class ServiceInfoResponse {
        private String name;
        private String version;

        public ServiceInfoResponse(String name, String version) {
            this.name = name;
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
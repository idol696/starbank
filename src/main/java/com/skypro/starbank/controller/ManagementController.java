package com.skypro.starbank.controller;


import com.skypro.starbank.dto.ServiceInfoDTO;
import com.skypro.starbank.service.ManagementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления сервисом, включая статистику срабатывания правил рекомендаций,
 * очистку кэшей и предоставление информации о сервисе.
 */
@RestController
@RequestMapping("/management")
@Tag(name = "Management Controller", description = "API для управления сервисом, включая статистику, очистку кэшей и информацию о сервисе")
public class ManagementController {

    private final ManagementServiceImpl managementService;


    public ManagementController(ManagementServiceImpl managementService) {
        this.managementService = managementService;
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
            content = @Content(schema = @Schema(implementation = ServiceInfoDTO.class)))
    public ResponseEntity<ServiceInfoDTO> getServiceInfo() {
        return ResponseEntity.ok(managementService.getServiceInfo());
    }
}
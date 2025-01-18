package com.skypro.starbank.controller;

import com.skypro.starbank.model.Product;
import com.skypro.starbank.model.Transaction;
import com.skypro.starbank.model.User;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления правилами.
 */
@Tag(name = "Правила", description = "API для работы с правилами")
@RestController
@RequestMapping("/rules")
public class RuleController {
    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * Получить все правила.
     *
     * @return Список всех правил.
     */
    @Operation(summary = "Получить все правила")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class,
                            description = "Список правил",
                            example = "[{\"productId\": \"product1\", \"conditions\": [{\"condition\": \"condition1\"}, {\"condition\": \"condition2\"}]}]")
            )
    )
    @GetMapping
    public ResponseEntity<List<RuleSet>> getAllRules() {
        List<RuleSet> rules = ruleService.getAllRules();
        return new ResponseEntity<>(rules, HttpStatus.OK);
    }

//    /**
//     * Получить правила по идентификатору продукта.
//     *
//     * @param productId Идентификатор продукта.
//     * @return Правила для данного продукта.
//     */
//    @Operation(summary = "Получить правила по идентификатору продукта")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешный запрос",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = RuleSet.class, description = "Правила",
//                                    example = "{\"productId\": \"product1\", \"conditions\": [{\"condition\": \"condition1\"}, {\"condition\": \"condition2\"}]}")
//                    )
//            ),
//            @ApiResponse(responseCode = "404", description = "Правила не найдены", content = @Content)
//    })
//    @GetMapping("/{productId}")
//    public ResponseEntity<RuleSet> getRulesByProductId(@Parameter(description = "Идентификатор продукта", example = "product1")
//                                                       @PathVariable String productId) {
//        RuleSet ruleSet = ruleService.getRulesByProductId(productId);
//        return new ResponseEntity<>(ruleSet, HttpStatus.OK);
//    } закомментированно, тк метода этого нет в сервисе

    /**
     * Установить правила.
     *
     * @param rules Список правил для установки.
     */
    @Operation(summary = "Установить правила")
    @ApiResponse(responseCode = "201", description = "Правила успешно установлены")
    @PostMapping("/set")
    public ResponseEntity<Void> setRules(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Список правил для установки", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class,
                            example = "[{\"productId\": \"product1\", \"conditions\": [{\"condition\": \"condition1\"}, {\"condition\": \"condition2\"}]}]")
            )
    ) @RequestBody List<RuleSet> rules) {
        ruleService.setRules(rules);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Сохранить правила.
     *
     * @return Сообщение об успешном запуске сохранения.
     */
    @Operation(summary = "Сохранить правила")
    @ApiResponse(responseCode = "202", description = "Сохранение правил запущено в фоновом режиме")
    @PostMapping("/save")
    public ResponseEntity<String> saveRules() {
        ruleService.saveRulesAsync();
        return new ResponseEntity<>("⏳ Сохранение правил запущено в фоновом режиме.", HttpStatus.ACCEPTED);
    }

//    /**
//     * Получить список всех пользователей
//     *
//     * @return Список всех пользователей
//     */
//    @Operation(summary = "Получить список всех пользователей")
//    @ApiResponse(responseCode = "200", description = "Успешный запрос",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = List.class,
//                            description = "Список пользователей",
//                            example = "[{\"id\": 1, \"name\": \"John Doe\"}, {\"id\": 2, \"name\": \"Jane Smith\"}]")
//            )
//    )
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    /**
//     * Получить список всех транзакций
//     *
//     * @return Список всех транзакций
//     */
//    @Operation(summary = "Получить список всех транзакций")
//    @ApiResponse(responseCode = "200", description = "Успешный запрос",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = List.class,
//                            description = "Список транзакций",
//                            example = "[{\"id\": 1, \"userId\": 1, \"amount\": 100.0}, {\"id\": 2, \"userId\": 2, \"amount\": 200.0}]")
//            )
//    )
//    @GetMapping("/transact")
//    public ResponseEntity<List<Transaction>> getAllTransactions() {
//        List<Transaction> transactions = transactionService.getAllTransactions();
//        return new ResponseEntity<>(transactions, HttpStatus.OK);
//    }
//
//    /**
//     * Получить список всех продуктов
//     *
//     * @return Список всех продуктов
//     */
//    @Operation(summary = "Получить список всех продуктов")
//    @ApiResponse(responseCode = "200", description = "Успешный запрос",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = List.class,
//                            description = "Список продуктов",
//                            example = "[{\"id\": 1, \"name\": \"Product 1\", \"description\": \"Description 1\"}, {\"id\": 2, \"name\": \"Product 2\", \"description\": \"Description 2\"}]")
//            )
//    )
//    @GetMapping("/products")
//    public ResponseEntity<List<Product>> getAllProducts() {
//        List<Product> products = productService.getAllProducts();
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }
//
//
//    /**
//     * Получить рекомендации для пользователя.
//     *
//     * @param userId Идентификатор пользователя.
//     * @return Рекомендации для данного пользователя.
//     */
//    @Operation(summary = "Получить рекомендации для пользователя")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешный запрос",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = RecommendationDTO.class, description = "Рекомендации",
//                                    example = "{\"user_id\": 1,\"recommendations\": [{\"name\": \"Product 1\", \"id\": 1, \"text\": \"Description 1\"}]}")
//                    )
//            ),
//            @ApiResponse(responseCode = "404", description = "Рекомендации не найдены", content = @Content)
//
//    })
//    @GetMapping("/recommendation/{userId}")
//    public ResponseEntity<RecommendationDTO> getRecommendations( @Parameter(description = "Идентификатор пользователя", example = "1")@PathVariable Long userId) {
//        RecommendationDTO recommendation = recommendationService.getRecommendationsForUser(userId);
//        return new ResponseEntity<>(recommendation, HttpStatus.OK);
//    }

}

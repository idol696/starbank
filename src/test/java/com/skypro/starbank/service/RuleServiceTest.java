package com.skypro.starbank.service;

import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RuleServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private RuleServiceImpl ruleService;

    @BeforeEach
    void setUp() {
        // Загружаем путь к `rules-test.json`
        File rulesFile = getRulesTestFile();
        assertNotNull(rulesFile, "⚠ Файл rules-test.json не найден в test/resources!");

        // Создаём RuleService вручную, передавая путь к `rules-test.json`
        ruleService = new RuleServiceImpl(transactionRepository, rulesFile.getAbsolutePath());
    }

    private File getRulesTestFile() {
        try {
            return new ClassPathResource("rules-test.json").getFile();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке test/resources/rules-test.json", e);
        }
    }

    @Test
    void shouldLoadRulesFromJson() {
        List<RuleSet> rules = ruleService.getAllRules();
        assertNotNull(rules);
        assertFalse(rules.isEmpty());
        assertEquals(2, rules.size());
    }

    @Test
    void shouldReturnRuleSetForExistingProduct() {
        RuleSet ruleSet = ruleService.getRulesByProductId("test-and-1");
        assertNotNull(ruleSet);
        assertEquals("AND Product", ruleSet.getName());
    }

    @Test
    void shouldReturnNullForNonExistingProduct() {
        RuleSet ruleSet = ruleService.getRulesByProductId("non-existing-id");
        assertNull(ruleSet);
    }

    @Test
    void shouldCorrectlyEvaluateAndCondition() {
        when(transactionRepository.userHasProduct("test-user", "DEBIT")).thenReturn(true);
        when(transactionRepository.getTotalDeposits("test-user", "SAVING")).thenReturn(6000.0);

        RuleSet ruleSet = ruleService.getRulesByProductId("test-and-1");
        assertNotNull(ruleSet);
        boolean result = ruleService.checkRulesForUser("test-user", ruleSet);
        assertTrue(result);

        verify(transactionRepository, times(1)).userHasProduct("test-user", "DEBIT");
        verify(transactionRepository, times(1)).getTotalDeposits("test-user", "SAVING");
    }

    @Test
    void shouldCorrectlyEvaluateOrCondition() {
        when(transactionRepository.userHasProduct("test-user", "CREDIT")).thenReturn(false);
        when(transactionRepository.getTotalExpenses("test-user", "DEBIT")).thenReturn(120000.0);

        RuleSet ruleSet = ruleService.getRulesByProductId("test-or-1");
        assertNotNull(ruleSet);
        boolean result = ruleService.checkRulesForUser("test-user", ruleSet);
        assertTrue(result);

        verify(transactionRepository, times(1)).userHasProduct("test-user", "CREDIT");
        verify(transactionRepository, times(1)).getTotalExpenses("test-user", "DEBIT");
    }

}

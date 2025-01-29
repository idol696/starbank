package com.skypro.starbank.service;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.repository.RuleSetRepository;
import com.skypro.starbank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class RuleServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RuleSetRepository ruleSetRepository;

    @InjectMocks
    private RuleServiceImpl ruleService;

    private UUID testProductId1;
    private UUID testProductId2;
    private UUID testProductId3;
    private UUID testProductId4;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testProductId1 = UUID.randomUUID();
        testProductId2 = UUID.randomUUID();
        testProductId3 = UUID.randomUUID();
        testProductId4 = UUID.randomUUID();
        testUserId = UUID.fromString("f37ba8a8-3cd5-4976-9f74-2b21f105da67");
        List<Rule> rulesList = List.of(new Rule("USER_OF",List.of("DEBIT"),false));
        List<Rule> rulesList2 = List.of(rulesList.get(0),new Rule("TRANSACTION_SUM_COMPARE",List.of("DEBIT", "DEPOSIT", ">", "1000"),false));
        List<Rule> rulesList3 = List.of(rulesList.get(0),new Rule("TRANSACTION_SUM_COMPARE",List.of("INVEST", "EXPENSE", ">", "1000"),false));
        List<Rule> rulesList4 = List.of(rulesList.get(0),new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",List.of("DEBIT", ">"),false));

        RuleSet ruleSet1 = new RuleSet(testProductId1, "Test1", "Desc1", rulesList);
        RuleSet ruleSet2 = new RuleSet(testProductId2, "Test2", "Desc2", rulesList2);
        RuleSet ruleSet3 = new RuleSet(testProductId3, "Test3", "Desc3", rulesList3);
        RuleSet ruleSet4 = new RuleSet(testProductId4, "Test4", "Desc4", rulesList4);

        when(ruleSetRepository.findAll()).thenReturn(List.of(ruleSet1, ruleSet2));
        when(ruleSetRepository.findByProductId(testProductId1)).thenReturn(Optional.of(ruleSet1));
        when(ruleSetRepository.findByProductId(testProductId2)).thenReturn(Optional.of(ruleSet2));
        when(ruleSetRepository.findByProductId(testProductId3)).thenReturn(Optional.of(ruleSet3));
        when(ruleSetRepository.findByProductId(testProductId4)).thenReturn(Optional.of(ruleSet4));
     }

    @Test
    void shouldLoadRulesFromDatabase() {
        List<RuleSet> rules = ruleService.getAllRules();
        assertNotNull(rules);
        assertFalse(rules.isEmpty());
        assertEquals(2, rules.size());

        verify(ruleSetRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnRuleSetForExistingProduct() {
        RuleSet ruleSet = ruleService.getRulesByProductId(testProductId1.toString());
        assertNotNull(ruleSet);
        assertEquals("Test1", ruleSet.getProductName());

        verify(ruleSetRepository, times(1)).findByProductId(testProductId1);
    }

    @Test
    void shouldReturnNullForNonExistingProduct() {
        RuleSet ruleSet = ruleService.getRulesByProductId(String.valueOf(UUID.randomUUID()));
        assertEquals(ruleSet, new RuleSet());

        verify(ruleSetRepository, times(1)).findByProductId(any());
    }

    @Test
    void shouldCorrectlyEvaluateCondition() {
        when(transactionRepository.userHasProduct(testUserId.toString(), "DEBIT")).thenReturn(true);

        RuleSet ruleSet = ruleService.getRulesByProductId(testProductId1.toString());
        System.out.println(ruleSet);
        assertNotNull(ruleSet);
        boolean result = ruleService.checkRulesForUser(testUserId.toString(), ruleSet);
        assertTrue(result);

        verify(transactionRepository, times(1)).userHasProduct(testUserId.toString(), "DEBIT");
    }

    @Test
    void shouldCorrectlyEvaluateTwoCondition() {
        when(transactionRepository.userHasProduct(testUserId.toString(), "DEBIT")).thenReturn(true);
        when(transactionRepository.getTotalDeposits(testUserId.toString(), "DEBIT")).thenReturn(1000.99);

        RuleSet ruleSet = ruleService.getRulesByProductId(String.valueOf(testProductId2));
        assertNotNull(ruleSet);
        boolean result = ruleService.checkRulesForUser(testUserId.toString(), ruleSet);
        assertTrue(result);

        verify(transactionRepository, times(1)).userHasProduct(testUserId.toString(), "DEBIT");
        verify(transactionRepository, times(1)).getTotalDeposits(testUserId.toString(), "DEBIT");
    }

    @Test
    void shouldCorrectlyEvaluateExpenseCondition() {
        when(transactionRepository.userHasProduct(testUserId.toString(), "DEBIT")).thenReturn(true);
        when(transactionRepository.getTotalExpenses(testUserId.toString(), "INVEST")).thenReturn(1000.99);

        RuleSet ruleSet = ruleService.getRulesByProductId(testProductId3.toString());
        System.out.println(ruleSet);
        assertNotNull(ruleSet);
        boolean result = ruleService.checkRulesForUser(testUserId.toString(), ruleSet);
        assertTrue(result);

        verify(transactionRepository, times(1)).userHasProduct(testUserId.toString(), "DEBIT");
        verify(transactionRepository, times(1)).getTotalExpenses(testUserId.toString(), "INVEST");
    }

    @Test
    void shouldCorrectlyEvaluateWithdrawCondition() {
        when(transactionRepository.userHasProduct(testUserId.toString(), "DEBIT")).thenReturn(true);
        when(transactionRepository.getTotalDeposits(testUserId.toString(), "DEBIT")).thenReturn(1000.99);
        when(transactionRepository.getTotalExpenses(testUserId.toString(), "DEBIT")).thenReturn(1000.00);

        RuleSet ruleSet = ruleService.getRulesByProductId(testProductId4.toString());
        System.out.println(ruleSet);
        assertNotNull(ruleSet);
        boolean result = ruleService.checkRulesForUser(testUserId.toString(), ruleSet);
        assertTrue(result);

        verify(transactionRepository, times(1)).userHasProduct(testUserId.toString(), "DEBIT");
        verify(transactionRepository, times(1)).getTotalDeposits(testUserId.toString(), "DEBIT");
        verify(transactionRepository, times(1)).getTotalExpenses(testUserId.toString(), "DEBIT");
    }

}

package com.skypro.starbank.service;


import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.model.rules.RuleSet;
import com.skypro.starbank.repository.RuleSetRepository;
import com.skypro.starbank.repository.RuleStatRepository;
import com.skypro.starbank.repository.TransactionRepository;
import com.skypro.starbank.service.rulehandlers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RuleServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RuleSetRepository ruleSetRepository;

    @Mock
    private RuleStatRepository ruleStatRepository;

    @Mock
    private UserOfHandler userOfHandler;

    @Mock
    private ActiveUserOfHandler activeUserOfHandler;

    @Mock
    private TransactionSumCompareHandler transactionSumCompareHandler;

    @Mock
    private TransactionSumCompareDepositWithdrawHandler transactionSumCompareDepositWithdrawHandler;

    @InjectMocks
    private RuleServiceImpl ruleService;

    private UUID testUserId;
    private UUID testProductId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.fromString("f37ba8a8-3cd5-4976-9f74-2b21f105da67");
        testProductId = UUID.randomUUID();

        Map<String, RuleHandler> mockRuleHandlers = Map.of(
                "USER_OF", userOfHandler,
                "ACTIVE_USER_OF", activeUserOfHandler,
                "TRANSACTION_SUM_COMPARE", transactionSumCompareHandler,
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", transactionSumCompareDepositWithdrawHandler
        );

        ruleService = new RuleServiceImpl(ruleSetRepository, ruleStatRepository, mockRuleHandlers);
    }

    @Test
    void shouldEvaluateUserOfRule() {
        Rule rule = new Rule("USER_OF", List.of("DEBIT"), false);
        when(userOfHandler.evaluate(testUserId.toString(), rule)).thenReturn(true);

        boolean result = ruleService.checkRulesForUser(testUserId.toString(), new RuleSet(testProductId, "Test", "Desc", List.of(rule)));

        assertTrue(result);
        verify(userOfHandler, times(1)).evaluate(testUserId.toString(), rule);
    }

    @Test
    void shouldEvaluateActiveUserOfRule() {
        Rule rule = new Rule("ACTIVE_USER_OF", List.of("DEBIT"), false);
        when(activeUserOfHandler.evaluate(testUserId.toString(), rule)).thenReturn(true);

        boolean result = ruleService.checkRulesForUser(testUserId.toString(), new RuleSet(testProductId, "Test", "Desc", List.of(rule)));

        assertTrue(result);
        verify(activeUserOfHandler, times(1)).evaluate(testUserId.toString(), rule);
    }

    @Test
    void shouldEvaluateTransactionSumCompareRule() {
        Rule rule = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">", "1000"), false);
        when(transactionSumCompareHandler.evaluate(testUserId.toString(), rule)).thenReturn(true);

        boolean result = ruleService.checkRulesForUser(testUserId.toString(), new RuleSet(testProductId, "Test", "Desc", List.of(rule)));

        assertTrue(result);
        verify(transactionSumCompareHandler, times(1)).evaluate(testUserId.toString(), rule);
    }

    @Test
    void shouldEvaluateTransactionSumCompareDepositWithdrawRule() {
        Rule rule = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), false);
        when(transactionSumCompareDepositWithdrawHandler.evaluate(testUserId.toString(), rule)).thenReturn(true);

        boolean result = ruleService.checkRulesForUser(testUserId.toString(), new RuleSet(testProductId, "Test", "Desc", List.of(rule)));

        assertTrue(result);
        verify(transactionSumCompareDepositWithdrawHandler, times(1)).evaluate(testUserId.toString(), rule);
    }
}

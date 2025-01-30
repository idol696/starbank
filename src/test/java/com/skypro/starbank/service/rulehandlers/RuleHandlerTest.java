package com.skypro.starbank.service.rulehandlers;


import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleHandlerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserOfHandler userOfHandler;

    @InjectMocks
    private ActiveUserOfHandler activeUserOfHandler;

    @InjectMocks
    private TransactionSumCompareHandler transactionSumCompareHandler;

    @InjectMocks
    private TransactionSumCompareDepositWithdrawHandler transactionSumCompareDepositWithdrawHandler;

    private final String testUserId = "test-user";

    @BeforeEach
    void setUp() {
        userOfHandler = new UserOfHandler(transactionRepository);
        activeUserOfHandler = new ActiveUserOfHandler(transactionRepository);
        transactionSumCompareHandler = new TransactionSumCompareHandler(transactionRepository);
        transactionSumCompareDepositWithdrawHandler = new TransactionSumCompareDepositWithdrawHandler(transactionRepository);
    }

    @Test
    void shouldEvaluateUserOfHandler() {
        Rule rule = new Rule("USER_OF", List.of("DEBIT"), false);
        when(transactionRepository.userHasProduct(testUserId, "DEBIT")).thenReturn(true);

        boolean result = userOfHandler.evaluate(testUserId, rule);
        assertTrue(result);
        verify(transactionRepository, times(1)).userHasProduct(testUserId, "DEBIT");
    }

    @Test
    void shouldEvaluateActiveUserOfHandler() {
        Rule rule = new Rule("ACTIVE_USER_OF", List.of("DEBIT"), false);
        when(transactionRepository.userHasProductCount(testUserId, "DEBIT")).thenReturn(6);

        boolean result = activeUserOfHandler.evaluate(testUserId, rule);
        assertTrue(result);
        verify(transactionRepository, times(1)).userHasProductCount(testUserId, "DEBIT");
    }

    @Test
    void shouldEvaluateTransactionSumCompareHandler() {
        Rule rule = new Rule("TRANSACTION_SUM_COMPARE", List.of("DEBIT", "DEPOSIT", ">", "1000"), false);
        when(transactionRepository.getTotalDeposits(testUserId, "DEBIT")).thenReturn(1500.0);

        boolean result = transactionSumCompareHandler.evaluate(testUserId, rule);
        assertTrue(result);
        verify(transactionRepository, times(1)).getTotalDeposits(testUserId, "DEBIT");
    }

    @Test
    void shouldEvaluateTransactionSumCompareDepositWithdrawHandler() {
        Rule rule = new Rule("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", List.of("DEBIT", ">"), false);
        when(transactionRepository.getTotalDeposits(testUserId, "DEBIT")).thenReturn(2000.0);
        when(transactionRepository.getTotalExpenses(testUserId, "DEBIT")).thenReturn(1000.0);

        boolean result = transactionSumCompareDepositWithdrawHandler.evaluate(testUserId, rule);
        assertTrue(result);
        verify(transactionRepository, times(1)).getTotalDeposits(testUserId, "DEBIT");
        verify(transactionRepository, times(1)).getTotalExpenses(testUserId, "DEBIT");
    }
}


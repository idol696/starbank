package com.skypro.starbank.service.rulehandlers;

import com.skypro.starbank.model.rules.Rule;
import com.skypro.starbank.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
public class TransactionSumCompareDepositWithdrawHandler extends MasterHandler {

     public TransactionSumCompareDepositWithdrawHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public boolean evaluate(String userId, Rule rule) {
        String productType = rule.getArgument(0);
        String operator = rule.getArgument(1);

        double totalDeposits = getTotalAmount(userId, productType, "DEPOSIT");
        double totalWithdrawals = getTotalAmount(userId, productType, "EXPENSE");

        boolean result = compare(totalDeposits, operator, totalWithdrawals);
        logger.debug("✅ TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW {} -> {} {} {} -> {}", productType, totalDeposits, operator, totalWithdrawals, result);
        return result;
    }
}

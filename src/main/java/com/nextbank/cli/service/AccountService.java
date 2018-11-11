package com.nextbank.cli.service;

import com.nextbank.cli.domain.Account;
import com.nextbank.cli.domain.AccountOperation;
import com.nextbank.cli.domain.Customer;
import com.nextbank.cli.enums.AccountOperationType;
import com.nextbank.cli.helper.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AuthenticationService authenticationService;
    private final Messages messages;
    private final AtomicLong ids = new AtomicLong();

    public AccountService(AuthenticationService authenticationService, Messages messages) {
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    public void depositIntoCurrentAccount(BigDecimal amount) {

        this.validateOperationAmount(amount);

        final Account account = this.getCurrentAccount();

        final BigDecimal newBalance = account.getBalance()
                .add(amount).setScale(2, RoundingMode.HALF_EVEN);

        final var credit = new AccountOperation(ids.incrementAndGet(),
                new Date(), amount, newBalance, AccountOperationType.CREDIT);
        LOG.info("Account {} has a new credit operation {}", account.getAccountNumber(), credit);
        account.getJournal().add(credit);

        account.setBalance(newBalance);

        LOG.info("Deposit of {} into account {}, its new balance is {}",
                amount, account.getAccountNumber(), account.getBalance());
    }

    public void withdrawFromCurrentAccount(BigDecimal amount) {

        this.validateOperationAmount(amount);

        final Account account = this.getCurrentAccount();
        BigDecimal newBalance = account.getBalance()
                .subtract(amount).setScale(2, RoundingMode.HALF_EVEN);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            LOG.debug("Unauthorised withdraw of {} from account {}",
                    amount, account.getAccountNumber());
            throw new IllegalStateException(this.messages.get("unauthorized.withdraw"));
        }

        final var debit = new AccountOperation(ids.incrementAndGet(),
                new Date(), amount, newBalance, AccountOperationType.DEBIT);
        LOG.info("Account {} has a new debit operation {}", account.getAccountNumber(), debit);
        account.getJournal().add(debit);

        account.setBalance(newBalance);

        LOG.info("Withdraw of {} from account {}, its new balance is {}",
                amount, account.getAccountNumber(), account.getBalance());
    }

    private Account getCurrentAccount() {

        Account connectedCustomerAccount = null;

        if (authenticationService.isConnected()) {
            final Optional<Customer> connectedCustomer = authenticationService.getConnectedCustomer();
            if (connectedCustomer.isPresent()) {
                connectedCustomerAccount = connectedCustomer.get().getAccount();
            }
        }

        if (connectedCustomerAccount == null) {
            LOG.error("Account unavailable");
            throw new IllegalStateException(this.messages.get("account.unavailable"));
        }

        return connectedCustomerAccount;
    }

    private void validateOperationAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            LOG.error("Trying to make operation with an invalid amount of {}", amount);
            throw new IllegalArgumentException(this.messages.get("amount.invalid"));
        }
    }
}

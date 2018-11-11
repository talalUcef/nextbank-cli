package com.nextbank.cli.command;

import com.nextbank.cli.domain.AccountOperation;
import com.nextbank.cli.helper.ConsoleHelper;
import com.nextbank.cli.helper.Messages;
import com.nextbank.cli.service.AccountService;
import com.nextbank.cli.service.AuthenticationService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.math.BigDecimal;
import java.util.List;

@ShellComponent
class AccountCommands {

    private final ConsoleHelper console;
    private final AuthenticationService authenticationService;
    private final AccountService accountService;
    private final Messages messages;

    AccountCommands(ConsoleHelper console, AuthenticationService authenticationService,
                    AccountService accountService, Messages messages) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
        this.messages = messages;
    }

    @ShellMethod("Deposit money into your account")
    public void deposit(BigDecimal amount) {
        try {
            this.accountService.depositIntoCurrentAccount(amount);
            console.write(messages.get("deposit.success"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            this.console.write(e.getMessage());
        }
    }

    Availability depositAvailability() {
        return this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(messages.get("customer.not.connected.message"));
    }

    @ShellMethod("Withdraw money from your account")
    public void withdraw(BigDecimal amount) {
        try {
            this.accountService.withdrawFromCurrentAccount(amount);
            console.write(messages.get("withdraw.success"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            this.console.write(e.getMessage());
        }
    }

    Availability withdrawAvailability() {
        return this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(messages.get("customer.not.connected.message"));
    }

    @ShellMethod("Check your account journal")
    public void check() {
        try {
            List<AccountOperation> journal = this.accountService.getCurrentAccountJournal();
            console.write(journal);
        } catch (IllegalStateException e) {
            this.console.write(e.getMessage());
        }
    }

    Availability checkAvailability() {
        return this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(messages.get("customer.not.connected.message"));
    }
}
package com.nextbank.cli.command;

import com.nextbank.cli.console.ConsoleLog;
import com.nextbank.cli.domain.AccountOperation;
import com.nextbank.cli.helper.Messages;
import com.nextbank.cli.service.AccountService;
import com.nextbank.cli.service.AuthenticationService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
class AccountCommands {

    private final AuthenticationService authenticationService;
    private final AccountService accountService;
    private final Messages messages;

    AccountCommands(AuthenticationService authenticationService,
                    AccountService accountService, Messages messages) {
        this.authenticationService = authenticationService;
        this.accountService = accountService;
        this.messages = messages;
    }

    @ShellMethod("Deposit money into your account")
    @ShellMethodAvailability("accountCommandsAvailability")
    @ConsoleLog
    public List<String> deposit(String amount) {
        final List<String> messages = new ArrayList<>();
        try {
            this.accountService.depositIntoCurrentAccount(new BigDecimal(amount));
            messages.add(this.messages.get("deposit.success"));
        } catch (NumberFormatException e) {
            messages.add(this.messages.get("amount.invalid"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            messages.add(e.getMessage());
        }
        return messages;
    }

    @ShellMethod("Withdraw money from your account")
    @ShellMethodAvailability("accountCommandsAvailability")
    @ConsoleLog
    public List<String> withdraw(String amount) {
        final List<String> messages = new ArrayList<>();
        try {
            this.accountService.withdrawFromCurrentAccount(new BigDecimal(amount));
            messages.add(this.messages.get("withdraw.success"));
        } catch (NumberFormatException e) {
            messages.add(this.messages.get("amount.invalid"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            messages.add(e.getMessage());
        }
        return messages;
    }

    @ShellMethod("Check your account journal")
    @ShellMethodAvailability("accountCommandsAvailability")
    @ConsoleLog
    public List<?> check() {
        final List<Object> messages = new ArrayList<>();
        try {
            List<AccountOperation> journal = this.accountService.getCurrentAccountJournal();
            if(journal.isEmpty()){
                return new ArrayList<AccountOperation>();
            }
            messages.addAll(journal);
        } catch (IllegalStateException e) {
            messages.add(e.getMessage());
        }
        return messages;
    }

    public Availability accountCommandsAvailability() {
        return this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(messages.get("customer.not.connected.message"));
    }
}
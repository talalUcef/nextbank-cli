package com.nextbank.cli.command;

import com.nextbank.cli.console.ConsoleLog;
import com.nextbank.cli.exception.BadCredentialsException;
import com.nextbank.cli.helper.Messages;
import com.nextbank.cli.service.AuthenticationService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
class AuthenticationCommands {

    private final AuthenticationService authenticationService;
    private final Messages messages;

    AuthenticationCommands(AuthenticationService authenticationService,
                           Messages messages) {
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @ShellMethod("connect to your account")
    @ConsoleLog
    public List<String> connect(String u, String p) {
        final List<String> messages = new ArrayList<>();
        try {
            this.authenticationService.connect(u, p);
            messages.add(this.messages.get("customer.greeting", u));
            messages.add(this.messages.get("connection.success"));
            messages.add(this.messages.get("connected.available.commands"));
        } catch (BadCredentialsException e) {
            messages.add(e.getMessage());
        }
        return messages;
    }

    Availability connectAvailability() {
        return !this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(this.messages.get("customer.already.connected"));
    }

    @ShellMethod("disconnect from your account")
    @ConsoleLog
    public List<String> disconnect() {
        final List<String> messages = new ArrayList<>();
        String username = this.authenticationService.disconnect();
        messages.add(this.messages.get("customer.goodbye", username));
        return messages;
    }

    Availability disconnectAvailability() {
        return this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(messages.get("customer.not.connected.message"));
    }
}
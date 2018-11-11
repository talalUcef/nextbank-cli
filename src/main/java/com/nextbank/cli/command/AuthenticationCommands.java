package com.nextbank.cli.command;

import com.nextbank.cli.exception.BadCredentialsException;
import com.nextbank.cli.helper.ConsoleHelper;
import com.nextbank.cli.helper.Messages;
import com.nextbank.cli.service.AuthenticationService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
class AuthenticationCommands {

    private final ConsoleHelper console;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    AuthenticationCommands(ConsoleHelper console, AuthenticationService authenticationService,
                           Messages messages) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @ShellMethod("connect to your account")
    public void connect(String u, String p) {
        try {
            this.authenticationService.connect(u, p);
            this.console.write(this.messages.get("customer.greeting", u));
            this.console.write(this.messages.get("connection.success"));
            this.console.write(this.messages.get("connected.available.commands"));
        } catch (BadCredentialsException e) {
            this.console.write(e.getMessage());
        }
    }

    Availability connectAvailability() {
        return !this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(this.messages.get("customer.already.connected"));
    }

    @ShellMethod("disconnect from your account")
    public void disconnect() {
        String username = this.authenticationService.disconnect();
        this.console.write(this.messages.get("customer.goodbye", username));
    }

    Availability disconnectAvailability() {
        return this.authenticationService.isConnected() ?
                Availability.available() :
                Availability.unavailable(messages.get("customer.not.connected.message"));
    }
}
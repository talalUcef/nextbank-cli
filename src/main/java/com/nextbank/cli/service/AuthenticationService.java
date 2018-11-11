package com.nextbank.cli.service;

import com.nextbank.cli.domain.Customer;
import com.nextbank.cli.domain.Session;
import com.nextbank.cli.exception.BadCredentialsException;
import com.nextbank.cli.helper.Messages;
import com.nextbank.cli.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    private final CustomerRepository repository;
    private Session currentSession = null;
    private final Messages messages;
    private final AtomicLong ids = new AtomicLong();


    public AuthenticationService(CustomerRepository repository, Messages messages) {
        this.repository = repository;
        this.messages = messages;
    }

    public void connect(String username, String password) throws BadCredentialsException {

        LOG.debug("Connecting customer with username {}", username);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new BadCredentialsException(this.messages.get("connection.credentials.required"));
        }

        final Optional<Customer> customerOptional = repository.findCustomerByUsername(username);

        if (!customerOptional.isPresent()) {
            LOG.debug("Trying to connect with unknown username {}", username);
            throw new BadCredentialsException(messages.get("username.invalid", username));
        }

        final Customer customer = customerOptional.get();
        if (password.equals(customer.getPassword())) {
            this.currentSession = new Session(ids.incrementAndGet(), new Date(), username);
            LOG.debug("Customer  with username {} connected with success", username);
        } else {
            throw new BadCredentialsException(this.messages.get("password.invalid"));
        }
    }

    public String disconnect() {
        String username = StringUtils.EMPTY;
        if (this.currentSession != null) {
            this.currentSession.setEndDate(new Date());
            username = currentSession.getUsername();
            LOG.debug("Customer with username {} disconnected with success", username);
        }
        this.currentSession = null;
        return username;
    }

    Optional<Customer> getConnectedCustomer() {
        if (this.isConnected()) {
            return repository.findCustomerByUsername(currentSession.getUsername());
        }
        return Optional.empty();
    }

    public boolean isConnected() {
        return this.currentSession != null;
    }
}

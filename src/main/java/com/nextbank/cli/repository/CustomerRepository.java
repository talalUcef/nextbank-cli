package com.nextbank.cli.repository;

import com.nextbank.cli.domain.Account;
import com.nextbank.cli.domain.Customer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CustomerRepository implements InitializingBean {

    private final Map<Long, Customer> customers = new ConcurrentHashMap<>();

    public Optional<Customer> findCustomerByUsername(String userName) {
        return this.customers.values()
                .stream()
                .filter(p -> userName.equalsIgnoreCase(p.getUsername()))
                .findFirst();
    }

    @Override
    public void afterPropertiesSet() {
        final var ids = new AtomicLong();
        final Map<Long, Customer> customerMap =
                Stream.of("Guido van Rossum",
                        "Yukihiro Matsumoto",
                        "James Gosling",
                        "Dennis Ritchie",
                        "Bjarne Stroustrup",
                        "Rasmus Lerdorf",
                        "Larry Wall",
                        "Brendan Eich")
                        .map(name -> {
                            final String firstName = name.substring(0, name.indexOf(' ')).toLowerCase();
                            return new Customer(ids.incrementAndGet(), name, firstName, firstName,
                                    new Account(ids.incrementAndGet(), BigDecimal.ZERO));
                        })
                        .collect(Collectors.toMap(Customer::getId, c -> c));
        this.customers.putAll(customerMap);
    }
}
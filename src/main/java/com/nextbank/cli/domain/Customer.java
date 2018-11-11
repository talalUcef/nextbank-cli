package com.nextbank.cli.domain;

import java.util.Objects;

public class Customer {

    private final Long id;
    private final String name;
    private final String username;
    private final String password;
    private final Account account;

    public Customer(Long id, String name, String username, String password, Account account) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.account = account;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", account=").append(account);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Account getAccount() {
        return account;
    }
}
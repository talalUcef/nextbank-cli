package com.nextbank.cli.domain;

import com.nextbank.cli.enums.AccountOperationType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class AccountOperation {

    private Long id;
    private AccountOperationType type;
    private BigDecimal amount;
    private BigDecimal balance;
    private Date date;

    public AccountOperation(Long id, Date date, BigDecimal amount, BigDecimal balance,
                            AccountOperationType type) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountOperation{");
        sb.append("id=").append(this.id);
        sb.append(", type=").append(this.type);
        sb.append(", amount=").append(this.amount);
        sb.append(", balance=").append(this.balance);
        sb.append(", date=").append(this.date);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountOperation operation = (AccountOperation) o;
        return Objects.equals(id, operation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Date getDate() {
        return this.date;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public AccountOperationType getType() {
        return this.type;
    }
}

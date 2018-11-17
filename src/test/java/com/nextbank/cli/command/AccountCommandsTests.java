package com.nextbank.cli.command;

import com.nextbank.cli.domain.AccountOperation;
import com.nextbank.cli.helper.Messages;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountCommandsTests {

    @Autowired
    private Messages messages;

    @Autowired
    private Shell shell;

    @Before
    public void setup() {
        shell.evaluate(() -> "connect guido guido");
        shell.evaluate(() -> "deposit 1000");
    }

    @Test
    public void testValidDeposit() {
        List<String> messages = (List<String>) shell.evaluate(() -> "deposit 500");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("deposit.success"));
        List<AccountOperation> journal = (List<AccountOperation>) shell.evaluate(() -> "check");
        Assert.assertNotNull(journal);
        Assert.assertThat(journal.size(), Matchers.greaterThanOrEqualTo(2));
        final BigDecimal amount = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(journal.get(journal.size() - 1).getBalance(),
                journal.get(journal.size() - 2).getBalance().add(amount));
    }

    @Test
    public void testDepositWithInvalidAmount() {
        List<String> messages = (List<String>) shell.evaluate(() -> "deposit 500aa");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("amount.invalid"));
    }

    @Test
    public void testDepositWithNegativeAmount() {
        List<String> messages = (List<String>) shell.evaluate(() -> "deposit -500");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("amount.invalid"));
    }

    @Test
    public void testValidWithdraw() {
        List<String> messages = (List<String>) shell.evaluate(() -> "withdraw 200");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("withdraw.success"));
        List<AccountOperation> journal = (List<AccountOperation>) shell.evaluate(() -> "check");
        Assert.assertNotNull(journal);
        Assert.assertThat(journal.size(), Matchers.greaterThanOrEqualTo(2));
        final BigDecimal amount = BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(journal.get(journal.size() - 1).getBalance(),
                journal.get(journal.size() - 2).getBalance().subtract(amount));

    }

    @Test
    public void testWithdrawWithInvalidAmount() {
        List<String> messages = (List<String>) shell.evaluate(() -> "withdraw 500aa");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("amount.invalid"));
    }

    @Test
    public void testWithdrawWithNegativeAmount() {
        List<String> messages = (List<String>) shell.evaluate(() -> "withdraw -500");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("amount.invalid"));
    }

    @Test
    public void testWithdrawWithNegativeBalance() {
        List<String> messages = (List<String>) shell.evaluate(() -> "withdraw 2000");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("unauthorized.withdraw"));
    }

    @Test
    public void testCheck() {
        List<AccountOperation> journal = (List<AccountOperation>) shell.evaluate(() -> "check");
        Assert.assertNotNull(journal);
        Assert.assertNotEquals(journal.size(), 0);
        Assert.assertEquals(journal.get(0).getAmount(), BigDecimal.valueOf(1000));
    }
}
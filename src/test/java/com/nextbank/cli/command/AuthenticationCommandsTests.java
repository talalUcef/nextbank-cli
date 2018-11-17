package com.nextbank.cli.command;

import com.nextbank.cli.helper.Messages;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationCommandsTests {

    @Autowired
    private Messages messages;

    @Autowired
    private Shell shell;

    @Before
    public void setup() {
        shell.evaluate(() -> "disconnect");
    }

    @Test
    public void testConnectWithInvalidUsername() {
        List<String> messages = (List<String>) shell.evaluate(() -> "connect invalidUsername guido");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("username.invalid", "invalidUsername"));
    }

    @Test
    public void testConnectWithInvalidPassword() {
        List<String> messages = (List<String>) shell.evaluate(() -> "connect guido invalidPassword");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("password.invalid"));
    }

    @Test
    public void testConnectWithGoodCredentials() {
        List<String> messages = (List<String>) shell.evaluate(() -> "connect guido guido");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 3);
        Assert.assertEquals(messages.get(0), this.messages.get("customer.greeting", "guido"));
        Assert.assertEquals(messages.get(1), this.messages.get("connection.success"));
        Assert.assertEquals(messages.get(2), this.messages.get("connected.available.commands"));
    }

    @Test
    public void testDisconnect() {
        shell.evaluate(() -> "connect guido guido");
        List<String> messages = (List<String>) shell.evaluate(() -> "disconnect");
        Assert.assertNotNull(messages);
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0), this.messages.get("customer.goodbye", "guido"));
    }
}
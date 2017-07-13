package com.github.yagarea.chat.server;

import org.junit.Assert;
import org.junit.Test;


public class AuthenticatorTest {

    @Test
    public void testUserRegistered() throws Exception {
        Authenticator authenticator = new Authenticator("src/test/java/AuthTest.txt");
        Assert.assertTrue(authenticator.userIsRegistered("registered"));
    }

    @Test
    public void testNotUserRegistered() throws Exception {
        Authenticator authenticator = new Authenticator("src/test/java/AuthTest.txt");
        Assert.assertFalse(authenticator.userIsRegistered("unregisteredUser"));
    }

    @Test
    public void testAuthenticate() throws Exception {
        Authenticator authenticator = new Authenticator("src/test/java/AuthTest.txt");
        boolean authenticated = authenticator.authenticate("registered", "password");
        Assert.assertTrue(authenticated);
    }

    @Test
    public void testAuthenticateWithWrongLogin() throws Exception {
        Authenticator authenticator = new Authenticator("src/test/java/AuthTest.txt");
        Assert.assertFalse(authenticator.authenticate("wrongUserName", "WrongPassword"));
    }

    @Test
    public void registerUser() throws Exception {
        Authenticator authenticator = new Authenticator("src/test/java/AuthTest.txt");
        Assert.assertFalse(authenticator.userIsRegistered("unregistered"));
        authenticator.registerUser("unregistered", "password");
        Assert.assertTrue(authenticator.userIsRegistered("unregistered"));
    }


}
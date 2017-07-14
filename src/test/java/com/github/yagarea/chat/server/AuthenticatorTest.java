package com.github.yagarea.chat.server;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;


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
    public void testAuthenticateWithWrongPassword() throws Exception {
        Authenticator authenticator = new Authenticator("src/test/java/AuthTest.txt");
        Assert.assertFalse(authenticator.authenticate("registered", "WrongPassword"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        File tempAuthFile = File.createTempFile("AuthTest", "txt");
        tempAuthFile.deleteOnExit();

        Authenticator authenticator = new Authenticator(tempAuthFile.getAbsolutePath());
        authenticator.registerUser("registered", "password");
        Assert.assertFalse(authenticator.userIsRegistered("unregistered"));
        authenticator.registerUser("unregistered", "password");
        Assert.assertTrue(authenticator.userIsRegistered("unregistered"));
    }

    @Test
    public void testChangePassword() throws Exception {
        File tempAuthFile = File.createTempFile("AuthTest", "txt");
        tempAuthFile.deleteOnExit();

        Authenticator authenticator = new Authenticator(tempAuthFile.getAbsolutePath());
        authenticator.registerUser("registered", "oldPassword");
        Assert.assertTrue(authenticator.authenticate("registered", "oldPassword"));
        authenticator.changePassword("registered", "newPassword");
        Assert.assertFalse(authenticator.authenticate("registered", "password"));
        Assert.assertTrue(authenticator.authenticate("registered", "newPassword"));
    }
}
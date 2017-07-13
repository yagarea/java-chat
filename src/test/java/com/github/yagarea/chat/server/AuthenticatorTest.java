package com.github.yagarea.chat.server;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;


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
    public void testRegisterUser() throws Exception {
        File tempAuthFile = File.createTempFile("AuthTest", "txt");
        tempAuthFile.deleteOnExit();
        new PrintWriter(new FileOutputStream(tempAuthFile)).println("registered:5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

        Authenticator authenticator = new Authenticator(tempAuthFile.getAbsolutePath());
        Assert.assertFalse(authenticator.userIsRegistered("unregistered"));
        authenticator.registerUser("unregistered", "password");
        Assert.assertTrue(authenticator.userIsRegistered("unregistered"));
    }

    @Test
    public void testChangePassword() throws Exception {
        File tempAuthFile = File.createTempFile("AuthTest", "txt");
        tempAuthFile.deleteOnExit();
        new PrintWriter(new FileOutputStream(tempAuthFile)).println("registered:5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

        Authenticator authenticator = new Authenticator(tempAuthFile.getAbsolutePath());
        authenticator.changePassword("registered", "newPassword");
        Assert.assertFalse(authenticator.authenticate("registered", "password"));
        Assert.assertTrue(authenticator.authenticate("registered", "newPassword"));
    }
}
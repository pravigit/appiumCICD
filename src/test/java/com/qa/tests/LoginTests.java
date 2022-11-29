package com.qa.tests;

import com.pages.LoginPage;
import com.pages.ProductsPage;
import com.qa.BaseTest;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;

    @BeforeClass
    public void beforeClass() throws IOException {
        InputStream inputStream = null;
        try {
            String fileName = "data/loginusers.json";
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            JSONTokener jsonTokener = new JSONTokener(inputStream);
            loginUsers = new JSONObject(jsonTokener);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        closeApp();
        launchApp();
    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        loginPage = new LoginPage();
        System.out.println("\n" + "******** start test : " + method.getName());
    }

    @AfterMethod
    public void afterMethod() {

    }

    @Test
    public void invalidUserName() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
        loginPage.enterUserPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
        loginPage.pressLoginBtn();
        String actualText = loginPage.getErrorText();
        String expectedText = getStringHashMap().get("err_invalid_username_or_invalid_password");
        System.out.println("Actual Text: " + actualText);
        System.out.println("Expected Text: " + expectedText);
        Assert.assertEquals(actualText, expectedText, "Expected Error message is NOT displayed");
    }

    @Test
    public void invalidPassword() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterUserPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginBtn();
        String actualText = loginPage.getErrorText();
        String expectedText = getStringHashMap().get("err_invalid_username_or_invalid_password");
        System.out.println("Actual Text: " + actualText);
        System.out.println("Expected Text: " + expectedText);
        Assert.assertEquals(actualText, expectedText, "Expected Error message is NOT displayed");
    }

    @Test
    public void validLogin() {
        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterUserPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginBtn();
        String actualProductTitle = productsPage.getTitle();
        String expectedProductTitle = getStringHashMap().get("product_title");
        System.out.println(actualProductTitle);
        System.out.println(expectedProductTitle);
        Assert.assertEquals(actualProductTitle, expectedProductTitle, "Expected Products message is NOT displayed");
    }
}
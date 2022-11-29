package com.qa.tests;

import com.pages.LoginPage;
import com.pages.ProductDetailsPage;
import com.pages.ProductsPage;
import com.pages.SettingsPage;
import com.qa.BaseTest;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    ProductDetailsPage productDetailsPage;
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
        productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
                loginUsers.getJSONObject("validUser").getString("password"));
    }

    @AfterMethod
    public void afterMethod() {
        settingsPage = productsPage.pressSettingsButton();
        loginPage = settingsPage.pressLogOutButton();
    }

    @Test(priority = 0)
    public void productsOnProductPage() {
        SoftAssert softAssert = new SoftAssert();

        String SLBTitle = productsPage.getSLBTitle();
        softAssert.assertEquals(SLBTitle, getStringHashMap().get("product_page_slb_title"));

        String SLBPrice = productsPage.getSLBPrice();
        softAssert.assertEquals(SLBPrice, getStringHashMap().get("product_page_slb_price"));

        softAssert.assertAll();
    }

    @Test(priority = 1)
    public void productsOnProductDetailsPage() {
        SoftAssert softAssert = new SoftAssert();

        productDetailsPage = productsPage.pressSLBTitle();

        String SLBTitleText = productDetailsPage.getSLBTitleTxt();
        System.out.println(SLBTitleText);
        softAssert.assertEquals(SLBTitleText, getStringHashMap().get("product_details_page_slb_title"));

        String SLBProdDesc = productDetailsPage.getSLBTitleDescription();
        System.out.println(SLBProdDesc);
        softAssert.assertEquals(SLBProdDesc, getStringHashMap().get("product_details_page_product_description"));

        String SLBProductPrice = productDetailsPage.scrollToSLBProductPriceAndGetText();
        System.out.println(SLBProductPrice);
        softAssert.assertEquals(SLBProductPrice, getStringHashMap().get("product_details_page_product_price"));

        productsPage = productDetailsPage.clickBackToProducts();

        softAssert.assertAll();
    }
}

package com.pages;

import com.qa.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BaseTest {

    @AndroidFindBy(xpath = "//*[@content-desc='test-Username']")
    private MobileElement userNameTxtField;
    @AndroidFindBy(xpath = "//*[@content-desc='test-Password']")
    private MobileElement password;
    @AndroidFindBy(accessibility = "test-LOGIN")
    private MobileElement login;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    private MobileElement errorMessage;

    public LoginPage()
    {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public LoginPage enterUserName(String username) {
        clear(userNameTxtField);
        System.out.println("Login with " + username);
        sendKeys(userNameTxtField, username);
        return this;
    }

    public LoginPage enterUserPassword(String userPassword) {
        clear(password);
        System.out.println("password is " + userPassword);
        sendKeys(password, userPassword);
        return this;
    }

    public ProductsPage pressLoginBtn() {
        System.out.println("Press Login Button");
        click(login);
        return new ProductsPage();
    }

    public String getErrorText() {
        return getText(errorMessage);
    }

    public ProductsPage login(String userName, String password)
    {
        enterUserName(userName);
        enterUserPassword(password);
        System.out.println("Enter login details and Press Login Button");
        return pressLoginBtn();
    }
}
package com.pages;

import com.qa.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

public class SettingsPage extends BaseTest {

    @AndroidFindBy(accessibility = "test-LOGOUT")
    private MobileElement logOutButton;

    public SettingsPage()
    {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public LoginPage pressLogOutButton()
    {
        System.out.println("Press Logout Button");
        click(logOutButton);
        return new LoginPage();
    }
}

package com.qa;

import com.pages.SettingsPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class MenuPage extends BaseTest{

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    public MobileElement settingsButton;

    public SettingsPage pressSettingsButton()
    {

        System.out.println("Press Settings Button");
        click(settingsButton);
        System.out.println("Pressed Settings Button");
        return new SettingsPage();
    }
}

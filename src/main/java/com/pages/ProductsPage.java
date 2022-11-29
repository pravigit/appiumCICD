package com.pages;

import com.qa.MenuPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends MenuPage {

    @AndroidFindBy(xpath = "//*[@text='PRODUCTS']")
    public MobileElement product;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    public MobileElement slbTitle;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    public MobileElement slbPrice;

    public ProductsPage()
    {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public String getTitle() {
        return getText(product);
    }

    public String getSLBTitle() {
        return getText(slbTitle);
    }

    public String getSLBPrice() {
        return getText(slbPrice);
    }

    public ProductDetailsPage pressSLBTitle()
    {
        System.out.println("Press SLB Title");
        click(slbTitle);
        return new ProductDetailsPage();
    }

}
package com.pages;

import com.qa.MenuPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;

public class ProductDetailsPage extends MenuPage {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    private MobileElement slbTitleTxt;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    private MobileElement slbTitleDescription;

    @AndroidFindBy(accessibility = "test-Price")
    private MobileElement slbProductPrice;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-BACK TO PRODUCTS\"]/android.widget.TextView")
    private MobileElement backToProducts;

    public ProductDetailsPage()
    {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public String getSLBTitleTxt() {
        return getText(slbTitleTxt);
    }

    public String scrollToSLBProductPriceAndGetText()
    {
        return getText(scrollToElement());
    }

    public void scrollPage()
    {
        iosScrollToElement();
    }
    public String getSLBTitleDescription() {
        return getText(slbTitleDescription);
    }

    public ProductsPage clickBackToProducts() {
        System.out.println("Press Back to products");
        click(backToProducts);
        return new ProductsPage();
    }
}

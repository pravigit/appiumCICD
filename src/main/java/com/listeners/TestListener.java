package com.listeners;

import com.qa.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;


public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if (iTestResult.getThrowable() != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            iTestResult.getThrowable().printStackTrace(printWriter);
        }

        BaseTest baseTest = new BaseTest();

        Map<String, String> params = iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();

        File sourceFile = baseTest.getDriver().getScreenshotAs(OutputType.FILE);

        String createDirectory = System.getProperty("user.dir") +
                File.separator + "Screenshots" +
                File.separator + params.get("platformName") + "_" + params.get("platformVersion") + "_" + params.get("deviceName") +
                File.separator + baseTest.getDateTime() + File.separator + iTestResult.getTestClass().getRealClass().getSimpleName();

        String screenShotPath = createDirectory + File.separator + iTestResult.getName() + ".png";

        try {
            FileHandler.createDir(new File(createDirectory));
            FileHandler.copy(sourceFile, new File(screenShotPath));
            Reporter.log("This is the Sample screen shot");
            Reporter.log("<a href='" + screenShotPath + "'> <img src='" + screenShotPath + "'height='400' width='400'/> </a>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

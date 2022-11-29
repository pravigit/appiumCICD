package com.qa;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import com.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {

    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<Properties> properties = new ThreadLocal<>();
    protected static ThreadLocal<Map<String, String>> stringHashMap = new ThreadLocal<>();
    protected static ThreadLocal<String> platform = new ThreadLocal<>();
    protected static ThreadLocal<String> dateTime = new ThreadLocal<>();
    static Logger log = LogManager.getLogger(BaseTest.class.getName());

    private static AppiumDriverLocalService appiumDriverLocalService;

    TestUtils testUtils;

    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    public Properties getProperty() {
        return properties.get();
    }

    public void setProperties(Properties properties1) {
        properties.set(properties1);
    }

    public Map<String, String> getStringHashMap() {
        return stringHashMap.get();
    }

    public void setStringHashMap(Map<String, String> stringHashMap1) {
        stringHashMap.set(stringHashMap1);
    }

    public String getPlatform() {
        return platform.get();
    }

    public void setPlatform(String platform1) {
        platform.set(platform1);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(String dateTime1) {
        dateTime.set(dateTime1);
    }

    @BeforeSuite
    public void beforeSuite() {
        ThreadContext.put("ROUTING_KEY", "ServerLogs");
        // Uncomment for MAC
        // appiumDriverLocalService = getAppiumDriverLocalServiceCustom();

        // Uncomment for Windows
           appiumDriverLocalService = getAppiumDriverLocalServiceDefault();
        
            appiumDriverLocalService.start();
            appiumDriverLocalService.clearOutPutStreams(); // Server logs to NOT output to console
            log.info("Appium Server Started");
    }

    public AppiumDriverLocalService getAppiumDriverLocalServiceDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService getAppiumDriverLocalServiceCustom() {
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", " ");
        environment.put("ANDROID_HOME", "");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe")) // node Path
                .withAppiumJS(new File("C:\\Users\\Praveen_Eruvanti\\AppData\\Local\\Programs\\Appium Server GUI\\resources\\app\\node_modules\\appium\\build\\lib\\main.js")) // main.js path
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File("ServerLogs/server.log")));
    }

    @AfterSuite
    public void afterSuite() {
        appiumDriverLocalService.stop();
        log.info("Appium Server Stopped");
    }

    @BeforeMethod
    public void beforeMethod() {
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult iTestResult) throws IOException {
        BaseTest baseTest = new BaseTest();
        Map<String, String> params = iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
        if (iTestResult.getStatus() == 2) {
            String createDirectory = System.getProperty("user.dir") +
                    File.separator + "Videos" +
                    File.separator + params.get("platformName") + "_" + params.get("platformVersion") + "_" + params.get("deviceName") +
                    File.separator + baseTest.getDateTime() + File.separator + iTestResult.getTestClass().getRealClass().getSimpleName();
            FileOutputStream fileOutputStream = null;

            try {
                FileHandler.createDir(new File(createDirectory));
                fileOutputStream = new FileOutputStream(createDirectory + File.separator + iTestResult.getName() + ".mp4");
                fileOutputStream.write(Base64.getDecoder().decode(media));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fileOutputStream.close();
            }
        }

    }

    @Parameters({"emulator", "platformName", "udid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeTest
    public synchronized void beforeTest(@Optional("androidOnly") String emulator, String platformName, String udid, String deviceName,
                                        @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort,
                                        @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) throws IOException {
        log.info("This is Info Message");
        testUtils = new TestUtils();
        setDateTime(testUtils.dateTime());
        setPlatform(platformName);
        URL url;
        InputStream inputStream = null;
        InputStream stringsInputStream = null;
        Properties properties = new Properties();
        AppiumDriver driver;
        String strFile = "logs" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTING_KEY", strFile);

        try {
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            properties.load(inputStream);
            setProperties(properties);
            stringsInputStream = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            setStringHashMap(testUtils.parseStringXML(stringsInputStream));
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, platformName);
            desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            switch (platformName) {
                case "Android":
                    desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, properties.getProperty("androidAutomationName"));
                    desiredCapabilities.setCapability("appPackage", properties.getProperty("androidAppPackage"));
                    desiredCapabilities.setCapability("appActivity", properties.getProperty("androidAppActivity"));
                    desiredCapabilities.setCapability("udid", udid);      // For Real Device
                    if (emulator.equals("true")) { // For Emulator
                        desiredCapabilities.setCapability("avd", deviceName);
                    }
                    desiredCapabilities.setCapability("systemPort", systemPort);
                    desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);
                    desiredCapabilities.setCapability("app", System.getProperty("user.dir") + properties.getProperty("androidAppLocation"));
                    url = new URL(properties.getProperty("appiumURL") + "4723/wd/hub");
                    driver = new AndroidDriver(url, desiredCapabilities);
                    break;
                case "iOS":
                    desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, properties.getProperty("iOSAutomationName"));
                    desiredCapabilities.setCapability("bundleId", properties.getProperty("iOSBundleId"));
                    desiredCapabilities.setCapability("app", System.getProperty("user.dir") + properties.getProperty("iOSAppLocation"));
                    desiredCapabilities.setCapability("wdaLocalPort", wdaLocalPort);
                    desiredCapabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
                    url = new URL(properties.getProperty("appiumURL") + "4724/wd/hub");
                    driver = new IOSDriver(url, desiredCapabilities);
                    break;

                default:
                    throw new Exception("Invalid Platform + " + platformName);
            }
            setDriver(driver);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (stringsInputStream != null) {
                stringsInputStream.close();
            }
        }
    }

    public void waitForVisibility(MobileElement e) {
        WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.NORMAL_WAIT);
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void clear(MobileElement e) {
        waitForVisibility(e);
        e.clear();
    }

    public void click(MobileElement e) {
        waitForVisibility(e);
        e.click();
    }

    public void sendKeys(MobileElement e, String text) {
        waitForVisibility(e);
        e.sendKeys(text);
    }

    public String getAttribute(MobileElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getText(MobileElement e) {
        if (platform.equals("Android"))
            return getAttribute(e, "text");
        else if (platform.equals("iOS"))
            return getAttribute(e, "label");
        return null;
    }

    public void closeApp() {
        getDriver().closeApp();
    }

    public void launchApp() {
        getDriver().launchApp();
    }

    public MobileElement scrollToElement() {
        return (MobileElement) ((FindsByAndroidUIAutomator<?>) driver).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));");
    }

    public void iosScrollToElement() {
        RemoteWebElement remoteWebElement = (RemoteWebElement) getDriver().findElement(By.className(""));
        String elementID = remoteWebElement.getId();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("element", elementID);
        // OPTION 1 to SCROLL
        hashMap.put("direction", "down");
        // OPTION 2 to SCROLL   hashMap.put("predicateString", "label== 'ADD TO CART'");
        // OPTION 3 to SCROLL   hashMap.put("name", "test-ADD TO CART");
        // OPTION 4 to SCROLL   hashMap.put("toVisible","not an empty String"); Need to use driver.findElement(By.name) instead of className
        getDriver().executeScript("mobile:scroll", hashMap);
    }


    @AfterTest
    public void after() {
        getDriver().quit();
    }
}
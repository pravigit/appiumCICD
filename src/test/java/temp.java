import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class temp {

    public static void main(String[] args) throws MalformedURLException {
        AppiumDriver driver;
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "1362976800000L7");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
        desiredCapabilities.setCapability("appPackage", "com.swaglabsmobileapp");
        desiredCapabilities.setCapability("appActivity", "com.swaglabsmobileapp.SplashActivity");
       // desiredCapabilities.setCapability("app", "");
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        System.out.println("Test in middle");
        driver = new AndroidDriver(url, desiredCapabilities);
        String sessionID = driver.getSessionId().toString();
        System.out.println("Session :" + sessionID);
    }
}

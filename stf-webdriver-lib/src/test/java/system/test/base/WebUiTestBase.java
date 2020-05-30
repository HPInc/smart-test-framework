package system.test.base;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;

public class WebUiTestBase extends WireMockTestBase {

    protected String seleniumGridServerUrl;
    private ChromeOptions chromeOptions;
    protected DesiredCapabilities desiredCapabilities;

    @Parameters({
            "param_SeleniumGridHost",
            "param_SeleniumGridPort",
            "param_BaseGridUrl",
            "param_IsSelenoid",
            "param_BrowserName",
            "param_BrowserVersion"
    })
    @BeforeClass
    public void initializeWebUiClass(
            String param_SeleniumGridHost,
            String param_SeleniumGridPort,
            String param_BaseGridUrl,
            Boolean param_IsSelenoid,
            String param_BrowserName,
            String param_BrowserVersion) {

        chromeOptions = configureChromeOptions();
        desiredCapabilities = configureDesiredCapabilities(
                chromeOptions,
                param_BrowserName,
                param_BrowserVersion,
                param_IsSelenoid);

        seleniumGridServerUrl = String.format(param_BaseGridUrl, param_SeleniumGridHost, param_SeleniumGridPort);
    }

    private ChromeOptions configureChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--allow-running-insecure-content");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("window-size=1920,1080");
        chromeOptions.addArguments("enable-automation");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--dns-prefetch-disable");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return chromeOptions;
    }

    private DesiredCapabilities configureDesiredCapabilities(
            ChromeOptions chromeOptions,
            String browserName,
            String browserVersionString,
            boolean isSelenoid) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(browserName);
        desiredCapabilities.setVersion(browserVersionString);
        if (isSelenoid) {
            desiredCapabilities.setCapability("enableVNC", true);
        }
        desiredCapabilities.setCapability("name", "STF Web Ui demo tests");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return desiredCapabilities;
    }

    protected WebDriver startChromeBrowser(
            DesiredCapabilities desiredCapabilities,
            String seleniumGridServerUrl) throws MalformedURLException {
        return new RemoteWebDriver(new URL(seleniumGridServerUrl), desiredCapabilities);
    }
}
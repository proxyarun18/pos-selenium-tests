package com.framework.selenium.api.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class DriverInstance{
	private static final ThreadLocal<RemoteWebDriver> remoteWebdriver = new ThreadLocal<RemoteWebDriver>();
	private static final ThreadLocal<WebDriverWait> wait = new  ThreadLocal<WebDriverWait>();

	public void setWait() {
		wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(10)));
	}

	public WebDriverWait getWait() {
		return wait.get();
	}

	public void setDriver(String browser, boolean headless) throws MalformedURLException {		
		// Check if we should use local drivers (for local testing)
		boolean useLocal = Boolean.parseBoolean(System.getProperty("use.local.driver", "false"));
		
		switch (browser) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized"); 
			options.addArguments("--disable-notifications"); 
			options.setAcceptInsecureCerts(true);
			if (headless) {
				options.addArguments("--headless");
			}
			
			if (useLocal) {
				// Use local ChromeDriver for testing
				remoteWebdriver.set((RemoteWebDriver) new ChromeDriver(options));
			} else {
				// Use remote Selenium Grid (production)
				DesiredCapabilities dc = new DesiredCapabilities();
				dc.setBrowserName("chrome");
				dc.setPlatform(Platform.LINUX);
				options.merge(dc);
				remoteWebdriver.set(new RemoteWebDriver(new URL("http://4.213.224.7:32000/wd/hub"), options));
			}
			break;
		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addArguments("--disable-notifications"); 
			if (headless) {
				firefoxOptions.addArguments("--headless");
			}
			
			if (useLocal) {
				// Use local FirefoxDriver for testing  
				remoteWebdriver.set((RemoteWebDriver) new FirefoxDriver(firefoxOptions));
			} else {
				// Use remote Selenium Grid (production)
				DesiredCapabilities desiredCap = new DesiredCapabilities();
				desiredCap.setBrowserName("firefox");
				desiredCap.setPlatform(Platform.LINUX);
				firefoxOptions.merge(desiredCap);
				remoteWebdriver.set(new RemoteWebDriver(new URL("http://4.213.224.7:32000/wd/hub"), firefoxOptions));
			}
			break;
		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("--disable-notifications"); 
			edgeOptions.setAcceptInsecureCerts(true);
			if (headless) {
				edgeOptions.addArguments("--headless");
			}
			
			if (useLocal) {
				// Use local EdgeDriver for testing
				remoteWebdriver.set((RemoteWebDriver) new EdgeDriver(edgeOptions));
			} else {
				// Use remote Selenium Grid (production)
				DesiredCapabilities desiredCapEdge = new DesiredCapabilities();
				desiredCapEdge.setBrowserName("MicrosoftEdge");
				desiredCapEdge.setPlatform(Platform.LINUX);
				edgeOptions.merge(desiredCapEdge);
				remoteWebdriver.set(new RemoteWebDriver(new URL("http://4.213.224.7:32000/wd/hub"), edgeOptions));
			}
			break;	
		case "ie":
			remoteWebdriver.set((RemoteWebDriver) new InternetExplorerDriver());
		default:
			break;
		
		}
	}
	public RemoteWebDriver getDriver() {
		return remoteWebdriver.get();
	}
	
}

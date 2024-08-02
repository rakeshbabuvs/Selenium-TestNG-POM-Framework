package com.qa.opencart.factory;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppError;
import com.qa.opencart.exceptions.BrowserException;
import com.qa.opencart.exceptions.FrameworkException;
import com.qa.opencart.logger.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class DriverFactory {
    WebDriver driver;
    Properties prop;
    OptionsManager optionsManager;

    public static String highlight;

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

    /**
     * This is used to init the driver on the basis on given browser name.
     *
     * @param browserName
     */
    public WebDriver initDriver(Properties prop) {
        String browserName = prop.getProperty("browser");
       // System.out.println("browser name is : " + browserName);
        Log.info("browser name is :"+browserName);

        highlight = prop.getProperty("highlight");

        optionsManager = new OptionsManager(prop);

        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                if (Boolean.parseBoolean(prop.getProperty("remote"))){
                    // run the test in remote Docker machine
                    initRemoteDriver("chrome");
                    }
                else {
                    // run it in local
                    Log.info("Running tests in Local");
                    tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
                }
                break;
            case "firefox":
                if (Boolean.parseBoolean(prop.getProperty("remote"))){
                    initRemoteDriver("firefox");
                }
                else{
                    tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
                }
                break;
            case "edge":
                if (Boolean.parseBoolean(prop.getProperty("remote"))){
                    initRemoteDriver("edge");
                }
                else{
                    tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
                }
                break;
            case "safari":
                tlDriver.set(new SafariDriver());
                break;

            default:
              //  System.out.println("plz pass the right browser name..." + browserName);
                Log.error("plz pass the right browser name..." + browserName);
                throw new BrowserException(AppError.BROWSER_NOT_FOUND);
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        getDriver().get(prop.getProperty("url"));// loginPage

        return getDriver();

    }

    private void initRemoteDriver(String browserName){
       // System.out.println("Running Tests in Selenium GRID with browser :"+browserName);
        Log.info("Running Tests in Selenium GRID with browser :"+browserName);
        try {
        switch (browserName) {
            case "chrome":
                tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
                break;
            case "firefox":
                tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
                break;
            case "edge":
                tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
                break;
            default:
                System.out.println("Plz pass right browser on GRID..");
                throw new BrowserException(AppError.BROWSER_NOT_FOUND);
        }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * get the local thread copy of the driver
     * @return
     */
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    /**
     * this is used to init the properties from the .properties file
     *
     * @return this returns properties (prop)
     */
    public Properties initProp() {
        prop = new Properties();
        FileInputStream ip = null;
        // mvn clean install -Denv="qa"
        // mvn clean install

        String envName = System.getProperty("env");
       // System.out.println("running test suite on env--->" + envName);
        Log.info("running test suite on env--->" + envName);

        if (envName == null) {
           // System.out.println("env name is not given, hence running it on QA environment....");
            Log.info("env name is not given, hence running it on QA environment....");
            try {
                ip = new FileInputStream(AppConstants.CONFIG_FILE_PATH);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                switch (envName.trim().toLowerCase()) {
                    case "qa":
                        ip = new FileInputStream(AppConstants.CONFIG_FILE_PATH);
                        break;
                    case "stage":
                        ip = new FileInputStream(AppConstants.CONFIG_STAGE_FILE_PATH);
                        break;
                    case "dev":
                        ip = new FileInputStream(AppConstants.CONFIG_DEV_FILE_PATH);
                        break;
                    case "uat":
                        ip = new FileInputStream(AppConstants.CONFIG_UAT_FILE_PATH);
                        break;
                    case "prod":
                        ip = new FileInputStream(AppConstants.CONFIG_FILE_PATH);
                        break;

                    default:
                       // System.out.println("please pass the right env name.." + envName);
                        Log.error("please pass the right env name.." + envName);
                        throw new FrameworkException("===WRONGENVPASSED===");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }


    /**
     * take screenshot
     */

    public static String getScreenshot(String methodName) {

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);//temp location

        String path = System.getProperty("user.dir")+"/screenshots/"+methodName+"_"+System.currentTimeMillis()+".png";

        File destination = new File(path);

        try {
            FileHandler.copy(srcFile, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }



}

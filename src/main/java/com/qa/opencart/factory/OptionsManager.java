package com.qa.opencart.factory;

import com.qa.opencart.logger.Log;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Properties;

public class OptionsManager {

    private Properties prop;
    private ChromeOptions co;
    private FirefoxOptions fo;
    private EdgeOptions eo;

    public OptionsManager(Properties prop) {
        this.prop = prop;
    }

    public ChromeOptions getChromeOptions() {
        co = new ChromeOptions();

        if (Boolean.parseBoolean(prop.getProperty("headless"))) {
           // System.out.println("====Running tests in headless======");
            Log.info("====Running tests in headless======");
            co.addArguments("--headless");
        }
        if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
            co.addArguments("--incognito");
        }
        if (Boolean.parseBoolean(prop.getProperty("remote"))){
            co.setCapability("browserName","chrome");

        }

        return co;
    }

    public FirefoxOptions getFirefoxOptions() {
        fo = new FirefoxOptions();

        if (Boolean.parseBoolean(prop.getProperty("headless"))) {
           // System.out.println("====Running tests in headless======");
            Log.info("====Running tests in headless======");
            fo.addArguments("--headless");
        }
        if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
            fo.addArguments("--incognito");
        }
        if (Boolean.parseBoolean(prop.getProperty("remote"))){
            co.setCapability("browserName","forefox");

        }

        return fo;
    }

    public EdgeOptions getEdgeOptions() {
        eo = new EdgeOptions();

        if (Boolean.parseBoolean(prop.getProperty("headless"))) {
           // System.out.println("====Running tests in headless======");
            Log.info("====Running tests in headless======");
            eo.addArguments("--headless");
        }
        if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
            eo.addArguments("--inPrivate");
        }
        if (Boolean.parseBoolean(prop.getProperty("remote"))){
            co.setCapability("browserName","edge");

        }

        return eo;
    }

}


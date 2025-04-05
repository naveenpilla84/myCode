package Automation1;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class BaseTestSetup_PW extends BrowserLaunch_PW {
    Logger log = LogManager.getLogger(BaseTestSetup_PW.class);

    public void go(String url) {
        try {
            page.navigate(url);
        } catch (Exception e) {
            log.error("Failed to navigate to URL: " + url, e);
        }
    }

    public static Object getDataFromConfigFile(String key) throws IOException {
        try (FileInputStream file = new FileInputStream("src/main/resources/userpreference.properties")) {
            Properties prop = new Properties();
            prop.load(file);
            return prop.get(key);
        } catch (IOException e) {
            throw new IOException("Failed to get data from config file for key: " + key, e);
        }
    }

    public static Object getDataFromLocatorsFile(String key) throws IOException {
        try (FileInputStream file = new FileInputStream("src/main/resources/locator.properties")) {
            Properties prop = new Properties();
            prop.load(file);
            return prop.get(key);
        } catch (IOException e) {
            throw new IOException("Failed to get data from locators file for key: " + key, e);
        }
    }

    public List<Locator> getElementsList(String locatorName) throws IOException {
        try {
            String value = (String) getDataFromLocatorsFile(locatorName);
            String[] values = value.split("\\|");
            if (values.length < 2) {
                throw new IllegalArgumentException("Invalid locator format: " + value);
            }
            String locID = values[0].trim();
            String locator = values[1].trim();
            Locator byFirst = getByFromLocator(locID, locator);
            return byFirst.all();
        } catch (Exception e) {
            throw new IOException("Failed to get elements list for locator: " + locatorName, e);
        }
    }

    public void iClick(String locatorName) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.click();
            log.info("Clicked on element: " + locatorName);
        } catch (Exception e) {
            log.error("Failed to click on element: " + locatorName, e);
            throw new IOException("Failed to click on element: " + locatorName, e);
        }
    }

    public void iClick(String locator, String locator1, int time) throws IOException {
        try {
            Locator element1 = getWebelement(locator);
            Locator element2 = getWebelement(locator1);
            element1.click();
            element2.waitFor();
            element2.click();
            log.info("Clicked on elements");
        } catch (Exception e) {
            log.error("Failed to click on elements: " + locator + ", " + locator1, e);
            throw new IOException("Failed to click on elements", e);
        }
    }

    private Locator getByFromLocator(String locID, String locator) {
        try {
            return switch (locID.toLowerCase()) {
                case "id" -> page.locator("#" + locator);
                case "xpath" -> page.locator("xpath=" + locator);
                case "name" -> page.locator("[name='" + locator + "']");
                case "css" -> page.locator(locator);
                case "class" -> page.locator("." + locator);
                case "tag" -> page.locator(locator);
                case "linktext" -> page.locator("text=" + locator);
                case "partiallinktext" -> page.locator("text=" + locator);
                default -> {
                    log.error("Invalid locator type: " + locID);
                    throw new IllegalArgumentException("Invalid locator type: " + locID);
                }
            };
        } catch (Exception e) {
            log.error("Failed to get locator by: " + locID + ", " + locator, e);
            throw new RuntimeException("Failed to get locator by: " + locID + ", " + locator, e);
        }
    }

    public void iEnterText(String locatorName, String text) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.fill(text);
            log.info("Entered text in element: " + locatorName);
        } catch (Exception e) {
            log.error("Failed to enter text in element: " + locatorName, e);
            throw new IOException("Failed to enter text in element: " + locatorName, e);
        }
    }

    public void iDoubleClick(String locatorName) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.dblclick();
            log.info("Double-clicked on element: " + locatorName);
        } catch (Exception e) {
            log.error("Failed to double-click on element: " + locatorName, e);
            throw new IOException("Failed to double-click on element: " + locatorName, e);
        }
    }

    public void assertConditionCheck(String value) {
        try {
            Assert.assertEquals(page.title(), value);
        } catch (AssertionError e) {
            log.error("Assertion failed for value: " + value, e);
            throw e;
        }
    }

    public void locatorVisibleOrNot(String locatorName, int time) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(time));
            boolean isVisible = element.isVisible();
            if (!isVisible) {
                throw new RuntimeException("Locator is not visible: " + locatorName);
            }
        } catch (Exception e) {
            String msg = "Failed to find the locator to be visible: " + e.getMessage();
            log.error(msg, e);
            throw new IOException(msg, e);
        }
    }

    public void locatorClickableOrNot(String locatorName, int time) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(time));
            boolean isEnabled = element.isEnabled();
            log.info("Locator " + locatorName + " is clickable: " + isEnabled);
            if (!isEnabled) {
                throw new RuntimeException("Locator is not clickable: " + locatorName);
            }
        } catch (Exception e) {
            log.error("Failed to check if locator " + locatorName + " is clickable: " + e.getMessage(), e);
            throw new IOException("Failed to check if locator is clickable", e);
        }
    }

    public void takeScreenShot(String filename) throws IOException {
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filename + ".png")));
        } catch (Exception e) {
            log.error("Failed to take screenshot: " + filename, e);
            throw new IOException("Failed to take screenshot: " + filename, e);
        }
    }

    public void scrollToElement(String locatorName) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.scrollIntoViewIfNeeded();
        } catch (Exception e) {
            log.error("Failed to scroll to element: " + locatorName, e);
            throw new IOException("Failed to scroll to element: " + locatorName, e);
        }
    }

    public void refreshPage() {
        try {
            page.reload();
        } catch (Exception e) {
            log.error("Failed to refresh page", e);
        }
    }

    @BeforeTest
    public void launchBrowser() throws IOException {
        try {
            String browser = (String) getDataFromConfigFile("Browser_pw");
            setDriver(browser);
        } catch (Exception e) {
            log.error("Failed to launch browser", e);
            throw new IOException("Failed to launch browser", e);
        }
    }

    @AfterTest
    public void closeBrowser() {
        try {
            page.close();
        } catch (Exception e) {
            log.error("Failed to close browser", e);
        }
    }

    public void selectDropdown(String locatorName, String finalValue) throws IOException {
        try {
            Locator element = getWebelement(locatorName);
            element.selectOption(finalValue);
        } catch (Exception e) {
            log.error("Failed to select dropdown option: " + finalValue + " for locator: " + locatorName, e);
            throw new IOException("Failed to select dropdown option", e);
        }
    }

    public Locator getWebelement(String locatorName) throws IOException {
        try {
            String value = (String) getDataFromLocatorsFile(locatorName);
            String[] values = value.split("\\|");

            if (values.length < 2) {
                throw new IllegalArgumentException("Invalid locator format: " + value);
            }

            String locID = values[0].trim();
            String locator = values[1].trim();

            Locator byFirst = getByFromLocator(locID, locator);
            return byFirst.first();
        } catch (Exception e) {
            log.error("Failed to get web element for locator: " + locatorName, e);
            throw new IOException("Failed to get web element for locator: " + locatorName, e);
        }
    }

    public void passWithScreenShot(String fileName, String message) throws IOException {
        try {
            takeScreenShot(fileName);
            log.info(message);
        } catch (Exception e) {
            log.error("Failed to pass with screenshot: " + fileName, e);
            throw new IOException("Failed to pass with screenshot", e);
        }
    }

    public Page switchToChildWindow(Page parentWindow) {
        try {
            List<Page> allPages = page.context().pages();
            for (Page p : allPages) {
                if (!p.title().equals(parentWindow.title())) {
                    page = p;
                    System.out.println("Switched to Child Window: " + page.title());
                    return page;
                }
            }
        } catch (Exception e) {
            log.error("Failed to switch to child window", e);
        }
        return null;
    }

    public void closeCurrentWindAndSwitchToMain(Page parentWindow) {
        try {
            page.close(); // Close child window
            System.out.println("Child Window Closed");
            page = page.context().pages().get(0);
        } catch (Exception e) {
            log.error("Failed to close current window and switch to main", e);
        }
    }

    public void consoleMessage(String outPut) {
        try {
            System.out.println(outPut);
        } catch (Exception e) {
            log.error("Failed to print console message: " + outPut, e);
        }
    }
}
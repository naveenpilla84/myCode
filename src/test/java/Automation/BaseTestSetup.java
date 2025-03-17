package Automation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class BaseTestSetup extends BrowserLaunch {
    Logger log = LogManager.getLogger(BaseTestSetup.class);

    public void geturl(String url) {
        driver.get(url);
        System.out.println("Page title is: " + driver.getTitle());
    }

    public static Object getDataFromConfigFile(String key) throws IOException {
        FileInputStream file = new FileInputStream("src/main/resources/userpreference.properties");
        Properties prop = new Properties();
        prop.load(file);
        return prop.get(key);
    }
    public static Object getDataFromLocatorsFile(String key) throws IOException {
        FileInputStream file = new FileInputStream("src/main/resources/locator.properties");
        Properties prop = new Properties();
        prop.load(file);
        return prop.get(key);
    }
    public List<WebElement> getElementsList(String locatorName) throws IOException {
        String value = (String) getDataFromLocatorsFile(locatorName);
        String[] values = value.split("\\|");
        if (values.length < 2) {
            throw new IllegalArgumentException("Invalid locator format: " + value);
        }
        String locID = values[0].trim();
        String locator = values[1].trim();
        By byFirst = getByFromLocator(locID, locator);
        return driver.findElements(byFirst);
    }

    public void iClick(String locatorName) throws IOException {
        WebElement element = getWebelement(locatorName);
        element.click();
            log.info("Clicked on element: " + locatorName);
        }
    public void iClick(String  locator,String locator1,int time) throws IOException {
        WebDriverWait wait =new WebDriverWait(driver,Duration.ofSeconds(time));
        WebElement element1 = getWebelement(locator);
        WebElement element2 = getWebelement(locator1);

        element1.click();
        wait.until(ExpectedConditions.visibilityOf(element2));
        element2.click();

        log.info("Clicked on elements");
    }


    private By getByFromLocator(String locID, String locator) {
        return switch (locID.toLowerCase()) {
            case "id" -> By.id(locator);
            case "xpath" -> By.xpath(locator);
            case "name" -> By.name(locator);
            case "css" -> By.cssSelector(locator);
            case "class" -> By.className(locator);
            case "tag" -> By.tagName(locator);
            case "linktext" -> By.linkText(locator);
            case "partiallinktext" -> By.partialLinkText(locator);
            default -> {
                log.error("Invalid locator type: " + locID);
                throw new IllegalArgumentException("Invalid locator type: " + locID);
            }
        };
    }

    public void iEnterText(String locatorName,String text) throws IOException {
        try {
            WebElement element = getWebelement(locatorName);
            element.sendKeys(text);
            log.info("Entered text in element: " + locatorName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void iDoubleClick(String locatorName) throws IOException {
        WebElement element = getWebelement(locatorName);
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();

        log.info("Double-clicked on element: " + locatorName);
    }


     public void assertConditionCheck(String value){
        Assert.assertEquals(driver.getTitle(),value);
     }

    public void locatorVisibleOrNot(String locatorName, int time) throws IOException {
        try {
            WebElement element = getWebelement(locatorName);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            String msg = "Failed to find the locator to be visible: " + e.getMessage();
            throw new RuntimeException(msg);
        }
    }


    public void locatorClickableOrNot(String locatorName,int time) throws IOException {
        WebElement element = getWebelement(locatorName);
        WebDriverWait wait =new WebDriverWait(driver,Duration.ofSeconds(time));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    public void takeScreenShot(String filename) throws IOException {
        TakesScreenshot src=(TakesScreenshot)driver;
        File sourceFile =src.getScreenshotAs(OutputType.FILE);
        File destinationFile=new File("target/ScreenShots/"+filename+".jpg");
        FileHandler.copy(sourceFile, destinationFile);
    }

    public void scrollToElement(WebElement element){
        JavascriptExecutor js=  (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView(true);",element);

    }

    @Override
    public void refreshPage(){
       driver.navigate().refresh();
    }

    public void implicitWait(int time){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
    }

    @BeforeTest
    public void launchBrowser() throws IOException {
        String browser= (String) getDataFromConfigFile("Browser");
        setDriver(browser);
    }
    @AfterTest
    public void closeBrowser() {
         driver.quit();
    }

    public void selectDropdown(String locatorName, String finalValue) throws IOException {
        WebElement element = getWebelement(locatorName);
        Select sle = new Select(element);
        sle.selectByValue(finalValue);
    }

    public WebElement getWebelement(String locatorName) throws IOException {
        String value = (String) getDataFromLocatorsFile(locatorName);
        String[] values = value.split("\\|");

        if (values.length < 2) {
            throw new IllegalArgumentException("Invalid locator format: " + value);
        }

        String locID = values[0].trim();
        String locator = values[1].trim();

        By byFirst = getByFromLocator(locID, locator);
        return driver.findElement(byFirst);
    }

    public void action(){
        driver.switchTo().alert().accept();
        driver.switchTo().alert().dismiss();
        driver.switchTo().alert().sendKeys("xyz");
        driver.switchTo().alert().getText();

        Actions ac= new Actions(driver);
        ac.doubleClick(driver.findElement(By.id("ertyu"))).build().perform();
        ac.scrollToElement(driver.findElement(By.id("ertyu"))).build().perform();
        WebElement element=driver.findElement(By.id("ertyu"));
        Select sc=new Select(element);
        sc.selectByIndex(1);
        sc.getOptions();

    }
    public void passWithScreenShot(String fileName, String message) throws IOException {
        takeScreenShot(fileName);
        log.info(message);
    }

    public String getParentWindowHandle(){
       String parentWindow= driver.getWindowHandle();
       return parentWindow;
    }
    public void switchToChildWindow(String parentWindow) {
        Set<String> allWindows = driver.getWindowHandles();

        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(parentWindow)) {
                driver.switchTo().window(windowHandle);
                System.out.println("Switched to Child Window: " + driver.getTitle());
                return;
            }
        }
    }

    public void closeCurrentWindAndSwitchToMain(String parentWindow){
        driver.close(); // Close child window
        System.out.println("Child Window Closed");

        driver.switchTo().window(parentWindow); // Switch back to parent
        System.out.println("Switched back to Parent Window: " + driver.getTitle());
    }

    public void consoleMessage(String outPut){
        System.out.println(outPut);
    }

    public void addingWait(){
        WebDriverWait wait=  (WebDriverWait)driver;
//        wait.until(ExpectedConditions.)
    }

}
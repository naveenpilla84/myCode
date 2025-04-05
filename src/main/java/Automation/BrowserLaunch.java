package Automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class BrowserLaunch {
    public WebDriver driver;

    public void setDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        }
        if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        if (browser.equalsIgnoreCase("firefox")){
            WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
    }
    public WebDriver getDriver() {
        return driver;
    }
    public void refreshPage(){
        driver.navigate().refresh();
    }

     public static class ExcelUtils {

         public static String[] getLocatorFromExcel(String excelFilePath, String sheetName, String elementName) throws IOException, IOException {
             FileInputStream file = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(file);
             Sheet sheet = workbook.getSheet(sheetName);

             String[] locator = new String[2];
             for (Row row : sheet) {
                 Cell cell = row.getCell(0);
                 if (cell != null && cell.getStringCellValue().equalsIgnoreCase(elementName)) {
                     locator[0] = row.getCell(1).getStringCellValue(); // locID
                     locator[1] = row.getCell(2).getStringCellValue(); // locator value
                     break;
                 }
             }
             workbook.close();
             file.close();
             return locator;
         }
     }

 }

package Automation;


import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;

public class launchApplication extends BaseTestSetup{

    @Test(priority = 1)
    public void launchApplicationLogin() {
        try {
            implicitWait(10);
            String url = (String) getDataFromConfigFile("GreenCartURL");
            geturl(url);
            assertConditionCheck("GreenKart - veg and fruits kart");
            locatorVisibleOrNot("GreenKartTitle",10);
            passWithScreenShot("Homepage","Success with Launching application");
        }catch (Exception e){
            log.error("Login failed{}", String.valueOf(e));
        }
    }

    @Test(priority = 2)
    public void searchProduct() throws IOException {
        try {
            locatorClickableOrNot("SearchBar", 10);
            iClick("SearchBar");
            iEnterText("SearchBar", "Tomato");
            iClick("SearchBtn");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 3)
    public void topDealsCheck() throws IOException {
        try {
            locatorClickableOrNot("TopDealsLink",10);
            String window=getParentWindowHandle();
            iClick("TopDealsLink");
            switchToChildWindow(window);
            selectDropdown("topDealsDropdown","20");
            //Getting top 5 discount products
            int Size=5;
            List<WebElement> productNames=getElementsList("getTopProductName");
            List<WebElement> productPrices=getElementsList("getTopProductPrice");
            List<WebElement> productDiscounts=getElementsList("getTopProductDiscount");
            for (int i=1;i<productNames.size();i++){
                String productName=productNames.get(i).getText();
                String productPrice=productPrices.get(i).getText();
                String productDiscount=productDiscounts.get(i).getText();
                consoleMessage("Product:-"+productName+", Actual Price :-"+productPrice+", Discount :-"+productDiscount);
            }
            closeCurrentWindAndSwitchToMain(window);
            refreshPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 4)
    public void addProductsToCart()throws IOException {
        try {
            List<WebElement> productNames = getElementsList("ProductNames");
            List<WebElement> inputText=getElementsList("QuantityInput");
            String[] needItems = getDataFromConfigFile("Products").toString().split(";");
            List<WebElement> cartButtons = getElementsList("Cart");

            for (String item : needItems) {
                String[] productAndQuantity = item.split("\\|");
                if (productAndQuantity.length < 2) continue;

                String requiredProduct = productAndQuantity[0].trim();
                int quantity = Integer.parseInt(productAndQuantity[1].trim());

                for (int i = 0; i < productNames.size(); i++) {
                    String[] productDetails = productNames.get(i).getText().split("-");
                    if (productDetails.length > 1) {
                        String productName = productDetails[0].trim();
                        if (productName.equalsIgnoreCase(requiredProduct)) {
                            if(quantity>0) {
                                inputText.get(i).clear();
                                inputText.get(i).sendKeys(String.valueOf(quantity));
                                cartButtons.get(i).click();
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @Test(priority = 5)
    public void cartProcess() throws IOException {
        iClick("CartIcon");
        iClick("ProceedCart");
        String promo=getDataFromConfigFile("PromoCode").toString();
        iEnterText("PromoCode",promo);
        iClick("ApplyBtn");
        locatorVisibleOrNot("PromoSucsMsg",10);
        iClick("PlaceOrder");
    }

    @Test(priority = 6)
    public  void checkOutPage() throws IOException {
        locatorVisibleOrNot("CountryLabel",10);
        selectDropdown("CountryDrpDwn","India");
        iClick("checkBox");
        iClick("ProceedBtn");
        locatorVisibleOrNot("SuccessfullyMsg",10);
        consoleMessage("Successfully order placed");
    }
}

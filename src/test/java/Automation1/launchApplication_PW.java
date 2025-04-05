package Automation1;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;


public class launchApplication_PW extends BaseTestSetup_PW {

    private static final int LONG_WAIT = 90;
    protected Page offersTab;

    @Test(priority = 1)
    public void launchApplicationLogin() throws IOException {
            String url = (String) getDataFromConfigFile("GreenCartURL");
            go(url);
            assertConditionCheck("GreenKart - veg and fruits kart");
            locatorVisibleOrNot("GreenKartTitle",LONG_WAIT);
            passWithScreenShot("Homepage","Success with Launching application");
    }

    @Test(priority = 2)
    public void searchProduct() throws IOException {
            locatorClickableOrNot("SearchBar", LONG_WAIT);
            iClick("SearchBar");
            iEnterText("SearchBar", "Tomato");
            iClick("SearchBtn");
    }

    @Test(priority = 3)
    public void topDealsCheck() throws IOException {
           locatorClickableOrNot("TopDealsLink",LONG_WAIT);
            Page mainPage= page;
        offersTab= page.waitForPopup(() ->{
            try {
                iClick("TopDealsLink");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        page=offersTab;
            selectDropdown("topDealsDropdown","20");
            //Getting top 5 discount products
            int Size=5;
            List<Locator> productNames=getElementsList("getTopProductName");
            List<Locator> productPrices=getElementsList("getTopProductPrice");
            List<Locator> productDiscounts=getElementsList("getTopProductDiscount");
            for (int i=1;i<productNames.size();i++){
                String productName=productNames.get(i).textContent();
                String productPrice=productPrices.get(i).textContent();
                String productDiscount=productDiscounts.get(i).textContent();
                consoleMessage("Product:-"+productName+", Actual Price :-"+productPrice+", Discount :-"+productDiscount);
            }
            closeCurrentWindAndSwitchToMain(mainPage);
            refreshPage();
    }

    @Test(priority = 4)
    public void addProductsToCart()throws IOException {
            List<Locator> productNames = getElementsList("ProductNames");
            List<Locator> inputText=getElementsList("QuantityInput");
            String[] needItems = getDataFromConfigFile("Products").toString().split(";");
            List<Locator> cartButtons = getElementsList("Cart");

            for (String item : needItems) {
                String[] productAndQuantity = item.split("\\|");
                if (productAndQuantity.length < 2) continue;

                String requiredProduct = productAndQuantity[0].trim();
                int quantity = Integer.parseInt(productAndQuantity[1].trim());

                for (int i = 0; i < productNames.size(); i++) {
                    String[] productDetails = productNames.get(i).textContent().split("-");
                    if (productDetails.length > 1) {
                        String productName = productDetails[0].trim();
                        if (productName.equalsIgnoreCase(requiredProduct)) {
                            if(quantity>0) {
                                inputText.get(i).clear();
                                inputText.get(i).fill(String.valueOf(quantity));
                                cartButtons.get(i).click();
                            }
                        }
                    }
                }
            }
    }
    @Test(priority = 5)
    public void cartProcess() throws IOException {
        iClick("CartIcon");
        iClick("ProceedCart");
        String promo=getDataFromConfigFile("PromoCode").toString();
        iEnterText("PromoCode",promo);
        iClick("ApplyBtn");
        //locatorVisibleOrNot("PromoSuccessMsg",LONG_WAIT*3);
        iClick("PlaceOrder");
    }

    @Test(priority = 6)
    public  void checkOutPage() throws IOException {
        locatorVisibleOrNot("CountryLabel",LONG_WAIT*3);
        selectDropdown("CountryDrpDwn","India");
        iClick("checkBox");
        iClick("ProceedBtn");
        locatorVisibleOrNot("SuccessfullyMsg",LONG_WAIT*3);
        consoleMessage("Successfully order placed");
    }
}

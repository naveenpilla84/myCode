package Automation1;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.apache.hc.core5.http.Header;

public class BrowserLaunch_PW {
    protected Page page;
    protected Playwright pw;

    public BrowserLaunch_PW() {
        this.pw = Playwright.create();
    }

    public void setDriver(String browser) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);
        switch (browser.toLowerCase()) {
            case "chromium":
                page = pw.chromium().launch(options).newPage();
                break;
            case "firefox":
                page = pw.firefox().launch(options).newPage();
                break;
            case "webkit":
                page = pw.webkit().launch(options).newPage();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}

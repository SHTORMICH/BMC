package selenium;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DemoWebShopTest {
    @Test
    public void a() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://demowebshop.tricentis.com/");

        WebElement temp = driver.findElement(By.cssSelector("[class='top-menu'] [href='/computers']"));
        new Actions(driver).moveToElement(temp).perform();

        WebElement clickButton = driver.findElement(By.cssSelector("[class='top-menu'] [href='/desktops']"));
        clickButton.click();

        WebElement display = driver.findElement(By.cssSelector("[class='product-page-size'] [id='products-pagesize']"));
        display.click();

        WebElement numberOfPositionsOnPage = driver.findElement(By.cssSelector("[class='product-page-size'] [value='https://demowebshop.tricentis.com/desktops?pagesize=4']"));
        numberOfPositionsOnPage.click();

        WebElement sorter = driver.findElement(By.cssSelector("[class=product-sorting] [id='products-orderby']"));
        sorter.click();

        WebElement highToLow = driver.findElement(By.cssSelector("[class='product-sorting'] [value='https://demowebshop.tricentis.com/desktops?pagesize=4&orderby=11']"));
        highToLow.click();

        WebElement mostExpensiveItemAddCart = driver.findElement(By.cssSelector("[class='add-info'] [class='button-2 product-box-add-to-cart-button']"));
        mostExpensiveItemAddCart.click();

        driver.quit();
    }

    @Test
    public void b() {
        WebDriver driver = new ChromeDriver();
        String expectedTotalPriceItem = "2105.00";
        driver.get("https://demowebshop.tricentis.com/build-your-own-expensive-computer-2");

        WebElement processorFast = driver.findElement(By.xpath("//dt[contains(.,'Processor')]/following::ul[@class='option-list']/li[last()]/input"));
        processorFast.click();

        WebElement ram = driver.findElement(By.xpath("//dt[contains(.,'RAM')]/following::ul[@class='option-list']/li[last()]/input"));
        ram.click();

        List<WebElement> softwareElements = driver.findElements(By.xpath("//dt[contains(.,'Software')]/following::ul[@class='option-list']/li"));
        softwareElements.stream()
                .map(element -> element.findElement(By.cssSelector("input[type='checkbox']")))
                .filter(element -> !element.isSelected())
                .forEach(WebElement::click);

        WebElement buttonAddToCart = driver.findElement(By.cssSelector("[class='add-to-cart-panel'] [class*='add-to-cart-button']"));
        buttonAddToCart.click();

        Awaitility.await("Counter in cart was not increased")
                .atMost(10, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS)
                .until(() -> driver.findElement(By.cssSelector("[class='cart-qty']")).getText().replaceAll("\\W", "").equals("1"));

        WebElement closeNotification = driver.findElement(By.cssSelector("[class='bar-notification success'] [class='close']"));
        closeNotification.click();

        WebElement linkShoppingCart = driver.findElement(By.cssSelector("[class='header-links'] [class*='ico-cart']"));
        linkShoppingCart.click();

        WebElement actualTotalPriceItem = driver.findElement(By.cssSelector("[class='cart-total'] [class*='product-price order-total']"));

        Assert.assertEquals(expectedTotalPriceItem, actualTotalPriceItem.getText());

        WebElement removeItem = driver.findElement(By.cssSelector("[class='remove-from-cart'] [name='removefromcart']"));
        removeItem.click();

        WebElement buttonToUpdateCart = driver.findElement(By.cssSelector("[class='common-buttons'] [name='updatecart']"));
        buttonToUpdateCart.click();

        Awaitility.await("Counter in cart was not increased")
                .atMost(10, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS)
                .until(() -> driver.findElement(By.cssSelector("[class='cart-qty']")).getText().replaceAll("\\W", "").equals("0"));

        driver.quit();
    }
}

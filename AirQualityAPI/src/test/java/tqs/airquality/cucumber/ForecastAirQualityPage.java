package tqs.airquality.cucumber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
public class ForecastAirQualityPage {
    private WebDriver driver;

    @When("I go to the tab {string}")
    public void navigateTo(String url) {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.get(url);
    }

    @And("I want to get forecast data from  the city {string}, and country code {string}")
    public void search(String cityname, String countrycode){
        driver.findElement(By.id("outlined-margin-normal")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".MuiButton-label"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.id("outlined-margin-normal")).sendKeys(cityname + ","+ countrycode);
        driver.findElement(By.cssSelector(".MuiButton-label")).click();
    }
    @Then("data should appear over {int} times, one for each day")
    public void numberresults(int times){

        int counter = 0;
        {
            final WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }

        {
            List<WebElement> elements = driver.findElements(By.cssSelector("div:nth-child(3) > .row > .col-md-12 > .MuiTypography-root"));
            assert(elements.size() > 0);
            counter++;
        }
        {
            List<WebElement> elements = driver.findElements(By.cssSelector("div:nth-child(4) .col-md-12 > .MuiTypography-root"));
            assert(elements.size() > 0);
            counter++;
        }
        {
            List<WebElement> elements = driver.findElements(By.cssSelector("div:nth-child(5) .col-md-12 > .MuiTypography-root"));
            assert(elements.size() > 0);
            counter++;
        }
        assertThat(counter,is(times));
        driver.close();
    }
}

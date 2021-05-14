package tqs.airquality.cucumber;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.platform.engine.Cucumber;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CurrentAirQualityPage {

    private WebDriver driver;

    @When("I navigate to {string}")
    public void navigateTo(String url) {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.get(url);
    }

    @And("I want to get data from  the city {string}, and country code {string}")
    public void search(String cityname, String countrycode){
        driver.findElement(By.id("outlined-margin-normal")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".MuiButton-label"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.id("outlined-margin-normal")).sendKeys(cityname+","+countrycode);
        driver.findElement(By.cssSelector(".MuiButton-label")).click();
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }



    }
    @Then("data should appear, like for example, the coordinates {double},{double}")
    public void resultscoords(double lat, double lon){
        final WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiTypography-overline")));
        assertThat(driver.findElement(By.cssSelector(".MuiTypography-overline")).getText(), containsString(lat+ "," + lon));
    }
    @Then("Error should appear with the message {string}")
    public void error_result(String message) {
        final WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiTypography-root")));
        assertThat(driver.findElement(By.cssSelector(".MuiTypography-root")).getText(), is(message));
        driver.findElement(By.id("outlined-margin-normal")).click();
        driver.findElement(By.id("outlined-margin-normal")).click();
        {
            WebElement element = driver.findElement(By.id("outlined-margin-normal"));
        }
    }
    @And("some indicators like {string}, {string}, or {string}")
    public void resultsindicators(String ind1,String ind2, String ind3){
        final WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".row:nth-child(3) > .col-md-4:nth-child(2)" )));
        assertThat(driver.findElement(By.cssSelector(".row:nth-child(3) > .col-md-4:nth-child(2) .MuiCardHeader-root .MuiTypography-root")).getText(),
                containsString(ind1));
        assertThat(driver.findElement(By.cssSelector(".row:nth-child(3) > .col-md-4:nth-child(3) .MuiCardHeader-root .MuiTypography-root")).getText(),
                containsString(ind2));
        assertThat(driver.findElement(By.cssSelector(".row:nth-child(6) > .col-md-4:nth-child(1) .MuiCardHeader-root .MuiTypography-root")).getText(),
                containsString(ind3));
    }
}

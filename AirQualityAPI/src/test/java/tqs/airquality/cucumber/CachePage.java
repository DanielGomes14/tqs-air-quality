package tqs.airquality.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CachePage {
    private WebDriver driver;

    @When("I go over the page {string}")
    public void openpage(String  url) {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.get(url);
        driver.manage().window().setSize(new Dimension(1489, 727));
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @And("Type in {string},{string}")
    public void typeIn(String cityname, String countrycode) {
        driver.findElement(By.id("outlined-margin-normal")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".MuiButton-label"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.id("outlined-margin-normal")).sendKeys(cityname +","+ countrycode);
        driver.findElement(By.cssSelector(".MuiButton-label")).click();
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        {
            WebElement element = driver.findElement(By.cssSelector(".MuiButton-label"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector("li:nth-child(3) p")).click();
    }


    @Then("some statistics should appear like {string}, {string},{string}")
    public void stats(String n1, String n2, String n3) {


        final WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiGrid-root:nth-child(1) >" +
                " .MuiPaper-root > .MuiCardHeader-root .MuiTypography-root")));

        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(1) >" +
                " .MuiPaper-root > .MuiCardHeader-root .MuiTypography-root")).getText(), is(n1));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(2)" +
                " .MuiCardHeader-root .MuiTypography-root")).getText(), is(n2));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(3)" +
                " .MuiCardHeader-root .MuiTypography-root")).getText(), is(n3));
    }

    @And("all of them should have the values {int}")
    public void allOfThemShouldHaveTheValues(int arg0) {
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(1)" +
                " > .MuiPaper-root > .MuiCardContent-root > .MuiTypography-root")).getText(), is(String.valueOf(arg0)));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(2)" +
                " .MuiCardContent-root > .MuiTypography-root")).getText(), is(String.valueOf(arg0)));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(3)" +
                " .MuiCardContent-root > .MuiTypography-root")).getText(), is(String.valueOf(arg0)));
        driver.quit();
    }

    @And("number of requests and misses {int}, and number of hits {int}")
    public void resultStats(int arg0, int arg1) {
        final WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiGrid-root:nth-child(1)")));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(1)" +
                " > .MuiPaper-root > .MuiCardContent-root > .MuiTypography-root")).getText(), is(String.valueOf(arg0)));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(2)" +
                " .MuiCardContent-root > .MuiTypography-root")).getText(), is(String.valueOf(arg0)));
        assertThat(driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(3)" +
                " .MuiCardContent-root > .MuiTypography-root")).getText(), is(String.valueOf(arg1)));
        driver.quit();
    }


}

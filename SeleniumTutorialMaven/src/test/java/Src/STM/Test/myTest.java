package Src.STM.Test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;

@Listeners(Src.STM.Test.Listener.class)
public class myTest {
	static WebDriver driver;
	// variables
	String url = "http://probook.progideo.com";
	static String userName = "hem-maroc";
	static String password = "1234";
	static String spaceName = "mySpaceName17";
	static String Descspace = "DescriSpaceName";
	static String expectedTitle="Getting Started";
	WebDriverWait wait;
	
	  @BeforeClass
	  public void beforeClass() {
		  System.setProperty("webdriver.gecko.driver", "/home/progideo/eclipse/drivers/geckodriver/geckodriver");
			driver = new FirefoxDriver();
			driver.get(url);
			wait = new WebDriverWait (driver, 20);
			
	  }
  public static boolean waitforJSandJqueryToLoad() {
			WebDriverWait wait = new WebDriverWait (driver,70);
			ExpectedCondition<Boolean>  jqueryLoad=new  ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					try {
						return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active")==0);
					}catch (Exception e) {
						// TODO: handle exception
						return true;
					}
					
				}
			};
			ExpectedCondition<Boolean> jsLOad=new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor)driver).executeScript("return document.readyState").toString().equals("complete");
				}
			};
			return wait.until(jqueryLoad) && wait.until(jsLOad);
		}
	  private static WebElement findElement(final WebDriver driver, final By locator, final int timeoutSeconds) {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeoutSeconds, TimeUnit.SECONDS)
					.pollingEvery(1000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
			return wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver webDriver) {
					return driver.findElement(locator);
				}
			});
		}
//		@Parameters ("browser")
//		@BeforeMethod
//		public void beforeMethod(String browser) {
//			if (browser.equalsIgnoreCase("firefox")) {
//		        // Chemin vers le driver Gecko (pour Firefox uniquement)
//				 System.setProperty("webdriver.gecko.driver", "/home/progideo/eclipse/drivers/geckodriver/geckodriver");
//		        // Invocation du navigateur Firefox, qui sera identifié avec le nom "driver".
//		        driver = new FirefoxDriver();
//		} else if (browser.equalsIgnoreCase("chrome")) {
//				// Chemin vers le driver Chrome
//		        System.setProperty("webdriver.chrome.driver","/home/progideo/eclipse/drivers/chromedriver/chromedriver");
//		        // Invocation du navigateur Chrome, qui sera identifié avec le nom "driver".
//		        driver = new ChromeDriver();
//			}
//	        // Ouvrir la page "http://probook.progideo.com".
//			
//	        driver.get(url);
//	        wait = new WebDriverWait (driver, 20);
//		} 

  @Test
  public void login() {
	  System.out.println("login");
		driver.findElement(By.linkText("Sign in / up")).click();
		findElement(driver, By.id("login_username"), 5).sendKeys(userName);
		driver.findElement(By.id("login_password")).sendKeys(password);
		driver.findElement(By.id("loginBtn")).click();
		
		String title=driver.findElement(By.xpath("//div[@id='getting-started-panel']//div[@class='panel-heading']")).getText();
		Assert.assertEquals(title, expectedTitle);
		Reporter.log("Login Sucess",false);

	}
  @Test(enabled= false)
  public void create_new_space() throws InterruptedException {
	  System.out.println("create_new_space");
		waitforJSandJqueryToLoad();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("space-menu")));
		driver.findElement(By.id("space-menu")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='btn btn-info col-md-12']")));
		WebElement btnCreateSpace=driver.findElement(By.xpath("//a[@class='btn btn-info col-md-12']"));
		JavascriptExecutor executor=(JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click()", btnCreateSpace);
		waitforJSandJqueryToLoad();
		driver.findElement(By.id("space-name")).click();
		driver.findElement(By.id("space-name")).sendKeys(spaceName);
		driver.findElement(By.id("space-description")).sendKeys(Descspace);
		driver.findElement(By.xpath("//button[text()='Next']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='btn btn-primary']")));
		waitforJSandJqueryToLoad();
		driver.findElement(By.xpath("//a[@class='btn btn-primary']")).click();

	}
  @AfterTest
  public void afterTest() {
	  driver.close();
  }

}

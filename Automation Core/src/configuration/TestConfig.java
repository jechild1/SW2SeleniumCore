package configuration;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.Reporter;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.text.TextProducer;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.AutomationHelper;
import utilities.ExcelDataConfig;

/**
 * Class containing methods to set up browser type and configuration.
 * 
 * @author jesse.childress
 *
 */
public abstract class TestConfig {

	// Project URL
	private static String baseUrl = "";
	private static String currentFrame = "";

	// Mail Trap user name / pass
	protected static String mailTrapEmailAddress = "mmurray@sw2.net";
	protected static String mailTrapEmailPassword = "Hw8MXPskqnKLXxU";

	// The main webdriver used throughout a given project
	protected static WebDriver driver;

	// Timeout variable to be used
	protected static int NORMAL_TIMEOUT = 20;
	protected static int SHORT_TIMEOUT = 1;
	protected static long MICRO_TIMEOUT_MILLIS = 100;
	protected static int LONG_TIMEOUT = 50;

	// Holds a soft asserter object.
	public static EnhancedSoftAssert softAsserter;
	
	/*
	 * For this to work correctly, you must set up an a system variable in Windows
	 * for the locations in which you would like to save.
	 */
//	protected static String DEFAULT_FILE_PATH_FOR_SAVING = "C:\\Users\\jesse\\git\\sw2-qa-automation\\sw2QA\\Application Report Files\\";
	protected static String DEFAULT_FILE_PATH_FOR_SAVING = System.getenv("Eclipse-FilePathForSaving");
//	protected static String DEFAULT_FILE_PATH_FOR_SCREENSHOTS = "C:\\Users\\jesse\\git\\sw2-qa-automation\\sw2QA\\Screenshots\\";
	protected static String DEFAULT_FILE_PATH_FOR_SCREENSHOTS = System.getenv("Eclipse-ScreenshotsLocation");
	/**
	 * Abstract config constructor
	 * 
	 */
	public TestConfig() {

	}

	/**
	 * Abstract config constructor accepts the address from the page inheriting this
	 * abstract config
	 * 
	 * @param addressUrl
	 */
	public TestConfig(String addressUrl) {
		baseUrl = addressUrl;
	}

	/**
	 * Returns the base URL defined at runtime
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Returns the current URL
	 * 
	 * @return
	 */
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/**
	 * Loads the given page in a new browser instance
	 */
	public void loadPage() {

		driver = this.setDriver();
		launchAndConfigureBrowser(driver);
		// page factories do this, but need to re-initialize after loading a
		// page
		// TODO JESSE
		PageFactory.initElements(driver, this);
	}

	/**
	 * Closes the given page
	 */
	public void closePage() {
		driver.close();
	}

	/**
	 * This methods instantiates a WebDriver for use in the application.
	 * 
	 * @return WebDriver
	 */
	private WebDriver setDriver() {

		// get user-specified browser from test suite xml file
		String selectedBrowser = getSelectedBrowser();

		// default to edge if not user selected
		selectedBrowser = selectedBrowser != null ? selectedBrowser : "chrome";

		switch (selectedBrowser.toLowerCase()) {
		case "ie":
			// 32bit works better than 64
			WebDriverManager.iedriver().arch32().setup();

			// ignoring zoom settings - otherwise an error is thrown
			InternetExplorerOptions options = new InternetExplorerOptions();
			options.ignoreZoomSettings();
			driver = new InternetExplorerDriver(options);
			break;

		case "edge":
			
			WebDriverManager.edgedriver().setup();
			EdgeOptions edgeOptions = new EdgeOptions();
	
			HashMap<String, Object> edgePrefs = new HashMap<>();
			edgePrefs.put("plugins.always_open_pdf_externally", true); //Forces PDF to NOT open in its own window
			edgePrefs.put("download.default_directory", DEFAULT_FILE_PATH_FOR_SAVING); // Specifies download directory
			
			
			edgeOptions.setExperimentalOption("prefs", edgePrefs);
	
			driver = new EdgeDriver(edgeOptions);

//			WebDriverManager.edgedriver().setup();
//			driver = new EdgeDriver();
			break;

		case "chrome":
			
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();

			HashMap<String, Object> chromePrefs = new HashMap<>();
			chromePrefs.put("plugins.always_open_pdf_externally", true); //Forces PDF to NOT open in its own window
			chromePrefs.put("download.default_directory", DEFAULT_FILE_PATH_FOR_SAVING); // Specifies download directory
			
			
			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			
			driver = new ChromeDriver(chromeOptions);
			
//			WebDriverManager.chromedriver().setup();
//				ChromeOptions chromeOptions = new ChromeOptions();
//				
//				HashMap<String, Object> prefs = new HashMap<>();
//				prefs.put("plugins.aways_open_pdf_externally", true);
//				prefs.put("download.default_directory", "C:\\Users\\jesse\\git\\sw2-qa-automation\\sw2QA\\test-output\\Downloaded Files");
//				prefs.put("download.prompt_for_download", false);
//				prefs.put("download.directory_upgrade", true);
//				prefs.put("safebrowsing.enabled", true);
//
//								chromeOptions.setExperimentalOption("prefs", prefs);
//				
//				driver = new ChromeDriver(chromeOptions);
			break;

		case "firefox":
		default:
			
			WebDriverManager.firefoxdriver().setup();
			
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			FirefoxProfile firefoxProfile = new FirefoxProfile();
						
			firefoxProfile.setPreference("browser.download.folderList",2); //Use for the default download directory the last folder specified for a download
			firefoxProfile.setPreference("browser.download.dir", DEFAULT_FILE_PATH_FOR_SAVING); //Set the last directory used for saving a file from the "What should (browser) do with this file?" dialog.
			firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf"); //list of MIME types to save to disk without asking what to use to open the file
			firefoxProfile.setPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer
			
			firefoxProfile.setPreference("geo.enabled", true);
			firefoxProfile.setPreference("geo.provider.use_corelocation", true);
			firefoxProfile.setPreference("geo.prompt.testing", true);
			firefoxProfile.setPreference("geo.prompt.testing.allow", true);
			firefoxProfile.setPreference("geo.wifi.uri",
					"data:application/json , { \"status\": \"OK\", \"accuracy\": 100.0, \"location\": { \"lat\": 27.401680, \"lng\": -82.468524, \"latitude\": 27.401680, \"longitude\": -82.468524, \"accuracy\": 100.0 } }");

	
			
			firefoxOptions.setProfile(firefoxProfile);
			
			driver = new FirefoxDriver(firefoxOptions);

//			// Profiles and options are meant to provide a zip code to the browser so that
//			// we are not prompted with geo-location popups.
//			FirefoxProfile profile = new FirefoxProfile();
//			profile.setPreference("geo.enabled", true);
//			profile.setPreference("geo.provider.use_corelocation", true);
//			profile.setPreference("geo.prompt.testing", true);
//			profile.setPreference("geo.prompt.testing.allow", true);
//			profile.setPreference("geo.wifi.uri",
//					"data:application/json , { \"status\": \"OK\", \"accuracy\": 100.0, \"location\": { \"lat\": 27.401680, \"lng\": -82.468524, \"latitude\": 27.401680, \"longitude\": -82.468524, \"accuracy\": 100.0 } }");
//
//			FirefoxOptions ffoptions = new FirefoxOptions().setProfile(profile);
//
//			WebDriverManager.firefoxdriver().setup();
//			driver = new FirefoxDriver(ffoptions);
			break;
		}

		return driver;
	}

	/**
	 * This method handles the following:
	 * <ul>
	 * <li>Creating a global Soft Assert object
	 * <li>setting the browser properties and maximizing window
	 * <li>Setting the default timeout to wait for an page to load
	 * <li>Loading the browser
	 * </ul>
	 * 
	 * @param driver
	 */
	private void launchAndConfigureBrowser(WebDriver driver) {
		instantiateSoftAsserter();
		setBrowserProperties(driver);
		setTimeout(driver, NORMAL_TIMEOUT);
		loadPage(driver);
	}

	/**
	 * Gets the selected browser (ex. firefox, chrome, etc.) to use for testing
	 */
	protected String getSelectedBrowser() {
		ITestContext testContext = Reporter.getCurrentTestResult().getTestContext();

		HashMap<String, String> parameters = new HashMap<String, String>(
				testContext.getCurrentXmlTest().getAllParameters());

		return parameters.get("selectedBrowser");
	}

	/**
	 * Sets properties in the browser, such as mazimizing the screen.
	 * 
	 * @param driver
	 */
	private static void setBrowserProperties(WebDriver driver) {
		driver.manage().window().maximize();
	}

	/**
	 * Sets the length of time before timing out when looking for an object on the
	 * page. This method uses Seconds
	 * 
	 * @param driver
	 * @param timeoutLengthInSeconds
	 */
	protected static void setTimeout(WebDriver driver, int timeoutLengthInSeconds) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutLengthInSeconds));

	}
	
	/**
	 * Sets the length of time before timing out when looking for an object on the
	 * page. This method uses Milliseconds
	 * 
	 * @param driver
	 * @param timeoutLengthInMillis
	 */
	protected static void setTimeout(WebDriver driver, long timeoutLengthInMillis) {
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(timeoutLengthInMillis));
	}

	/**
	 * Creates a SoftAsserter object at test configuration level to be accessible
	 * throughout project.
	 */
	private static void instantiateSoftAsserter() {
		softAsserter = new EnhancedSoftAssert();
	}

	/**
	 * Loads a browser with the base URL as outlined int the variable above.
	 * 
	 * @param driver
	 */
	private static void loadPage(WebDriver driver) {
		driver.get(baseUrl);
	}

	// Dataset for use
	// TODO: this needs to be refactored to be configurable
	// private static String masterDataSets = "dataSets\\masterDataSets\\";

	private static String localDataSets = "dataSets\\localDataSets\\";

	/**
	 * Returns an instance of ExcelDataConfig based on file name and worksheet name
	 * passed in
	 * 
	 * @param fileName
	 * @param worksheetName
	 * @return ExcelDataConfig
	 */
	public ExcelDataConfig getExcelFile(String fileName, String worksheetName) {
		String excelFilePath = generateFullFileNameAndPath(fileName);
		return new ExcelDataConfig(excelFilePath, worksheetName);
	}

	/**
	 * Generates a full file name path to be used to find a data set. This uses data
	 * in the dataSets folder. We can specify here in one place if we want to use
	 * local data or master.
	 * 
	 * @param fileNameWithExtension
	 * @return String
	 */
	protected String generateFullFileNameAndPath(String fileNameWithExtension) {
		return new File(localDataSets + fileNameWithExtension).getAbsolutePath();

	}

	/**
	 * This method is to set the driver back to default content
	 * 
	 */
	protected void setDriverToDefaultContent() {
		setDriverToFrameByID(null);
	}

	/**
	 * This method is to point the driver at a different frame on the page by the
	 * frame's ID.
	 * 
	 * Sending an empty string or null will set back to default content
	 * 
	 * @param frameID
	 */
	protected void setDriverToFrameByID(String frameID) {
		if (frameID == null || frameID.isEmpty()) {
			driver.switchTo().defaultContent();
			currentFrame = "";

		} else if (!currentFrame.equals(frameID)) {

			// use this implementation rather than passing ID directly to
			// frame...
			// this runs much faster
			WebElement frameElement = driver.findElement(By.id(frameID));
			driver.switchTo().frame(frameElement);
			currentFrame = frameID;
		}
	}

	/**
	 * Set driver to a browser alert on the page
	 */
	protected Alert setDriverToAlert() {
		return driver.switchTo().alert();
	}

	/**
	 * Returns the page title of a given page.
	 * 
	 * @return String
	 */
	public String readPageTitle() {
		return driver.getTitle();
	}

	/**
	 * Click Cancel on browser alert message
	 * 
	 * NOTE: resets to default content after
	 */
	public void clickCancelOnAlert() {
		setDriverToAlert().dismiss();
		setDriverToDefaultContent();
	}

	/**
	 * Click OK on browser alert message
	 * 
	 * NOTE: resets to default content after
	 */
	public void clickOKOnAlert() {
		AutomationHelper.printMethodName();
		setDriverToAlert().accept();
		setDriverToDefaultContent();
	}

	/**
	 * Returns browser alert message
	 * 
	 * @return String
	 */
	public String readAlert() {
		AutomationHelper.printMethodName();
		String alert = setDriverToAlert().getText();
		setDriverToDefaultContent();
		return alert;
	}

	/**
	 * Generates a random paragraph based on size constraint and sentence count
	 * 
	 * @param stringSize    - max char count of string
	 * @param sentenceCount - max number of sentences to generate
	 * @return String
	 */
	public String getRandomText(int stringSize, int sentenceCount) {
		// Create generic text helper
		TextProducer tp = Fairy.create().textProducer();

		String paragraph = stringSize > 1 ? tp.limitedTo(stringSize - 1).paragraph(sentenceCount) : "";

		return paragraph + tp.randomString(1);
	}

	/**
	 * Returns a boolean status as to if the element exists or not.
	 * 
	 * @param locator
	 * @return boolean
	 */
	public boolean isWebElementPresent(By locator) {
		return !driver.findElements(locator).isEmpty();
	}

	/**
	 * Method to generate a random user role for use for logging in as a different
	 * user.
	 * 
	 * @return String
	 */
	public String generateRandomUserRole() {

		List<String> roleList = new ArrayList<String>();

		// Get a list of roles from the users datasheet.
		ExcelDataConfig usersFile = getExcelFile("users.xlsx", "AutomationUsers");

		int rowCount = usersFile.getRowCount();
		int columnIndex = usersFile.getColumnIndex("Role");

		// By starting at i = 1, we eliminate the header
		for (int i = 1; i <= rowCount; i++) {

			roleList.add(usersFile.getData(i, columnIndex));

		}

		//Removed the  rowCount + 1
		return roleList.get(AutomationHelper.generateRandomInteger(1, rowCount-1));

	}

}

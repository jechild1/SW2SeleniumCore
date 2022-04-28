package utilities;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.awaitility.Awaitility.await;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.invoke.WrongMethodTypeException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import configuration.TestConfig;

public class AutomationHelper extends TestConfig {

	/**
	 * This method causes the script to pause for a given amount of time (in seconds).
	 * 
	 * @param timeInSeconds
	 */
	public static void waitSeconds(int timeInSeconds) {
		// Convert to milliseconds
		timeInSeconds = timeInSeconds * 1000;
		try {
			Thread.sleep(timeInSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method causes the script to pause for a given amount of time in
	 * Milliseconds. E.g., 500 is half a second.
	 * 
	 * @param timeInMilliseconds
	 */
	public static void waitMillis(long timeInMilliseconds) {

		try {
			Thread.sleep((long) timeInMilliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method generates a random integer based on the low / high range values
	 * passed in. It is inclusive of the low and high range numbers.
	 * 
	 * @param lowRange
	 * @param highRange
	 * @return int
	 */
	public static int generateRandomInteger(int lowRange, int highRange) {
		int randomNum = ThreadLocalRandom.current().nextInt(lowRange, highRange + 1);
		return randomNum;
	}

	/**
	 * Returns the page title of a given page.
	 * 
	 * @return String
	 * @deprecated use readPageTitle against the page factory context instead
	 */
	@Deprecated
	public static String getPageTitle() {
		return driver.getTitle();
	}

//	/**
//	 * This method checks for the presence of the passed in page title, using an
//	 * explicit wait. It will wait for a maximum of 15 seconds before timing out.
//	 * This method is necessary because sometimes pages are a bit slow to load.
//	 * 
//	 * @param expectedPageTitle e.g. Home Page - Team Fitness Tracker
//	 */
//	public static void checkPageTitlePresent(String expectedPageTitle) {
//		Reporter.log("Starting page title check, expecting page: " + expectedPageTitle, true);
//
//		WebDriverWait wait = new WebDriverWait(driver, 15);
//		// String pageTitle = getPageTitle(driver);
////		wait.until(ExpectedConditions.titleContains(expectedPageTitle));
//
//		Reporter.log("Page title found.", true);
//		Reporter.log("", true);
//
//	}

	/**
	 * Method to escape special characters of a string.
	 * 
	 * @param originalString
	 * @return String escaped regular expression
	 */
	public static String escapeStringForRegEx(String originalString) {
		final StringBuilder result = new StringBuilder();

		final StringCharacterIterator iterator = new StringCharacterIterator(originalString);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			/*
			 * All literals need to have backslashes doubled.
			 */
			if (character == '.') {
				result.append("\\.");
			} else if (character == '\\') {
				result.append("\\\\");
			} else if (character == '?') {
				result.append("\\?");
			} else if (character == '*') {
				result.append("\\*");
			} else if (character == '+') {
				result.append("\\+");
			} else if (character == '&') {
				result.append("\\&");
			} else if (character == ':') {
				result.append("\\:");
			} else if (character == '{') {
				result.append("\\{");
			} else if (character == '}') {
				result.append("\\}");
			} else if (character == '[') {
				result.append("\\[");
			} else if (character == ']') {
				result.append("\\]");
			} else if (character == '(') {
				result.append("\\(");
			} else if (character == ')') {
				result.append("\\)");
			} else if (character == '^') {
				result.append("\\^");
			} else if (character == '$') {
				result.append("\\$");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	/**
	 * Removes instances of tab, new line, return and space in the string
	 * 
	 * @param originalString
	 * @return String
	 */
	public static String removeMarkupFromString(String originalString) {
		return originalString.replaceAll("[\\t\\n\\r\\s\\u00A0]+", " ").trim();
	}

	/**
	 * This method waits for a given page to be loaded completely.
	 * 
	 * @param timeOutInSeconds
	 */
	public static void waitForPageToLoad(int timeOutInSeconds) {

		Reporter.log("Waiting for page to load completely...", true);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean initiallyLoaded = false;

		// Perform an initial check for the page to be loaded.
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			Reporter.log("The page '" + getPageTitle() + "' is fully loaded now.", true);
			Reporter.log("", true);
			initiallyLoaded = true;
		}

		if (initiallyLoaded == false) {
			for (int i = 0; i < timeOutInSeconds; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				if (js.executeScript("return document.readyState").toString().equals("complete")) {
					Reporter.log("The page '" + getPageTitle() + "'is fully loaded now.", true);
					Reporter.log("", true);
					break;
				}
			}
		}
	}

	/**
	 * Method to print the current method name.
	 */
	public static void printMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		// 0 = getStatckTrace
		// 1 = this method name
		// 2 = method that calls this one
		StackTraceElement e = stacktrace[2];
		String methodName = e.getMethodName();

		Reporter.log("Method: " + methodName, true);
		Reporter.log("", true);
	}

	/**
	 * Method to print the current class name.
	 */
	public static void printClassName() {
		Reporter.log("<br>");
		Reporter.log("<b>Class: <u>" + Thread.currentThread().getStackTrace()[2].getClassName() + "</u></b>", true);

		Reporter.log("<br>");
		// Reporter.log("", true);
	}

	/**
	 * Scrolls to specified WebElement
	 * 
	 * @param webElement
	 */
	public static void scrollIntoView(WebElement webElement) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView(true);", webElement);
	}

	/**
	 * Scrolls to specified WebElement - use when "scrollIntoView" does not work
	 * NOTE: does not perform as fast
	 * 
	 * @param webElement
	 */
	public static void scrollIntoViewAction(WebElement webElement) {
		Actions actions = new Actions(driver);
		actions.moveToElement(webElement).perform();
	}

	/**
	 * This method will accept a String (most likely from a data sheet). It
	 * evaluates the string to see if: <br>
	 * <li>Normal date: No change
	 * <li>Text of TODAY: Returns system date of today
	 * <li>Positive number: Returns todays date plus the number passed in (positive
	 * number)
	 * <li>Negative number: Returns todays date minus the number passed in (negative
	 * number) <br>
	 * This method will also handle dates with times. It simply removes the time to
	 * do the date calculation, and then re-appends it.
	 * 
	 * @param date
	 * @return String
	 */
	public static String generateCalendarDate(String date) {

		AutomationHelper.printMethodName();

		String objectDate = "";

		// Process only if we have a value. Else just return empty string.
		if (!date.equals("")) {
			/*
			 * Three options can happen with date: (1) Normal date (2) Text of TODAY (3)
			 * Positive number for day increment (4) Negative number for decrement
			 */

			String regexForDates = "\\d{2}-\\d{2}-\\d{4}";
			String regexForNegative = "\\-\\d+";
			String regexForPositive = "\\d+";
//		String regexForDateAndTime = "\\d{2}-\\d{2}-\\d{4} \\d{1,2}:\\d{2} ([AaPp][Mm])";

			// Special case to handle passing in of date and time
			boolean dateAndTime = false;
			String time = "";

			if (date.endsWith("AM") || date.endsWith("PM") || date.endsWith("am") || date.endsWith("pm")) {
				dateAndTime = true;

				// e.g. 12-12-2020 12:15 PM
				// e.g 200 12:13 AM
				// e.g. -34 12:12 pm
				// e.g. TODAY 2:12 am
				// Spits on the first space, which will be the same even if we use a number,
				// today, or a real date
				time = date.substring(date.indexOf(" ") + 1, date.length());
				date = date.substring(0, date.indexOf(" "));
			}

			AutomationCalendar cal = new AutomationCalendar();

			if (date.matches(regexForDates)) {
				objectDate = date;
			}

			else if (date.matches(regexForPositive)) {
				objectDate = cal.getTodaysDatePlusDays(Long.valueOf(date));
			}

			else if (date.matches(regexForNegative)) {
				// Remove the "-" from the minus days. the minusDays method upstream doesn't
				// need it.
				date = date.replace("-", "");
				objectDate = cal.getTodaysDateMinusDays(Long.valueOf(date));
			}

			else if (date.equalsIgnoreCase("today")) {
				objectDate = cal.getTodaysDate();
			}

			else if (!AutomationHelper.isDateFormatValid("MM-dd-yyyy", date)
					|| !AutomationHelper.isDateFormatValid("MM-dd-yyyy h:mm a", date)) {
				throw new DateTimeException(
						"You must pass in a date in the correct format of MM-dd-yyyy or MM-dd-yyyy h:mm a");
			}

			if (dateAndTime) {
				objectDate = objectDate + " " + time;
			}
		}

		return objectDate;
	}

	/**
	 * Selects a date and Time from a calendar date picker object, provided that the
	 * date is in MM-dd-yyyy format. <br>
	 * Note: Use setCalendarDate when time is not needed.
	 * 
	 * @param calendarField - The field that is clicked that makes the date picker
	 *                      appear. Most likely a text box.
	 * @param dateAndTime   - The date in MM-dd-yyyy h:mm a format (01-15-2023 1:15
	 *                      PM).
	 */
	public static void setCalendarDateAndTime(WebElement calendarField, String dateAndTime) {

		// FIRST: assert that the dateAndTime field is in the correct format. This
		// method will throw a parse exception if it is not.
		AutomationHelper.isDateFormatValid("MM-dd-yyyy h:mm a", dateAndTime);

		// SECOND: split the dateAndTime field into it's separate components, date and
		// Time.
		String date = dateAndTime.substring(0, dateAndTime.indexOf(" "));
		String time = dateAndTime.substring(dateAndTime.indexOf(" ") + 1, dateAndTime.length());

		// Because we already have code to handle setting dates, call the
		// setCalendarDate method
		setCalendarDate(calendarField, date);

		// Now, lets find the time objects. The objects are always in 15 minute
		// increments. If the time is something OTHER than 00, 15, 30, or 45, it will
		// not be in the list. We need to let the user know to adjust their data.
		// The following line pulls the minutes after the :
		String minutes = time.substring(time.indexOf(":") + 1, time.indexOf(":") + 3);
		if (!Stream.of("00", "15", "30", "45").anyMatch(minutes::equalsIgnoreCase)) {
			throw new RuntimeException(
					"The minutes in your time must be in quarter increments, e.g. 00, 15, 30 or 45. Currently you have passed in "
							+ minutes);
		}

		// We can assume that the date picker object is already opened

		List<WebElement> times = driver.findElements(By.xpath("//div[@class='react-datepicker__time']//li"));

		// If the user passes in a time with a leading zero, e.g. 08:00 PM, we must
		// strip the leading zero off because the time in the date picker does not have
		// a leading zero.
		// The ^ anchors to the start of the string. The 0* means zero or more 0
		// characters (you could use 0+ as well). The replaceFirst just replaces all
		// those 0 characters at the start with nothing.
		time = time.replaceFirst("^0*", "");

		// Loop through all of the times in the list and select the one that matches.
		for (WebElement currentTimeObject : times) {
			if (currentTimeObject.getText().equalsIgnoreCase(time)) {
				currentTimeObject.click();
				break;
			}

		}

	}

	/**
	 * Selects a date from a calendar date picker object, provided that the date is
	 * in MM-dd-yyyy format. <br>
	 * Note: Use setCalendarDateAndTime when time objects are needed.
	 * 
	 * @param calendarField - The field that is clicked that makes the date picker
	 *                      appear. Most likely a text box.
	 * @param date          - The date in MM-dd-yyyy format.
	 */
	public static void setCalendarDate(WebElement calendarField, String date) {

		// Only process if we have a data value.
		if (!date.equals("")) {

			// Click the field to display the date picker object.
			calendarField.click();

			// Build a list of months. This will be used to cycle through months in date
			// pickers.
			List<String> monthList = Arrays.asList("January", "February", "March", "April", "May", "June", "July",
					"August", "September", "October", "November", "December");

			// Expected Date, Month and Year
			int expectedMonth;
			int expectedYear;
			String expectedDay = null;

			// Calendar Month and Year
			String calendarMonth = null;
			String calendarYear = null;
			boolean dateNotFound = true;

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH);
			LocalDate localDate = LocalDate.parse(date, formatter);

			// Set your expected date, month and year.
			expectedDay = String.valueOf(localDate.getDayOfMonth());
			expectedMonth = localDate.getMonthValue();
			expectedYear = localDate.getYear();

			// This loop will be executed continuously till dateNotFound Is true.
			while (dateNotFound) {

				// Finds the Month and Year in the date picker, e.g. July 2023
				WebElement monthAndYearInCalHeader = driver
						.findElement(By.xpath("//div[@class = 'react-datepicker__current-month']"));

				// The text of the Month and Year will be in "Janurary 2022" format. We must
				// split it on the space between the month and year.
				String[] monthYear = monthAndYearInCalHeader.getText().split(" ");

				calendarMonth = monthYear[0];
				calendarYear = monthYear[1];

				// If current selected month and year are same as expected month and
				// year then go Inside this condition.
				if (monthList.indexOf(calendarMonth) + 1 == expectedMonth
						&& (expectedYear == Integer.parseInt(calendarYear))) {
					// Call selectDate function with date to select and set
					// dateNotFound flag to false.
					selectDate(calendarMonth, expectedDay);
					dateNotFound = false;
				}

				// If current selected month and year are less than expected month
				// and year then go Inside this condition.
				else if (monthList.indexOf(calendarMonth) + 1 < expectedMonth
						&& (expectedYear == Integer.parseInt(calendarYear))
						|| expectedYear > Integer.parseInt(calendarYear)) {
					// Click on next button of date picker.
					driver.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
				}
				// If current selected month and year are greater than expected
				// month and year then go Inside this condition.
				else if (monthList.indexOf(calendarMonth) + 1 > expectedMonth
						&& (expectedYear == Integer.parseInt(calendarYear))
						|| expectedYear < Integer.parseInt(calendarYear)) {
					// Click on previous button of date picker.

					driver.findElement(By.xpath("//button[@aria-label='Previous Month']")).click();

				}
			}
		}
	}

	/**
	 * Sets a checkbox based on desired status
	 * 
	 * @param element
	 * @param checked
	 */
	public static void setCheckbox(WebElement element, boolean desiredStatus) {
		// Get current boolean status of checked or not checked.
		boolean checked = element.isSelected();

		if ((desiredStatus == true) && (checked == false)) {
			element.click();
		} else if ((desiredStatus == false) && (checked == true)) {
			element.click();
		}
	}

	/**
	 * Sets a Radio Button based on desired status
	 * 
	 * @param element
	 * @param checked
	 */
	public static void setRadioButton(WebElement element, boolean desiredStatus) {
		// Get current boolean status of selected or not
		boolean checked = element.isSelected();

		if ((desiredStatus == true) && (checked == false)) {
			element.click();
		} else if ((desiredStatus == false) && (checked == true)) {
			element.click();
		}
	}

	/**
	 * Methods to read the currently selected value of a check box object.
	 * 
	 * @param element
	 * @return returns true if selected; false if not
	 */
	public static boolean readCheckboxStaus(WebElement element) {
		return element.isSelected();
	}

	/**
	 * Methods to read the currently selected value of a Radio Button object.
	 * 
	 * @param element
	 * @return returns true if selected; false if not
	 */
	public static boolean readRadioButtonStaus(WebElement element) {
		return element.isSelected();
	}

	/**
	 * Sets the specified text within the specified text field web element NOTE:
	 * this clears any existing text that may have been in the field
	 * 
	 * @param element
	 * @param text
	 */
	public static void setTextField(WebElement element, String text) {
		element.clear();

		// Sometimes Clear doesn't happen correctly
		while (!element.getAttribute("value").equals("")) {
			element.sendKeys(Keys.CONTROL + "A");
			element.sendKeys(Keys.DELETE);
		}

		element.sendKeys(text);
	}

	private static void selectDate(String calMonth, String date) {

		List<WebElement> monthDays = driver.findElements(By.xpath(
				"//div[@class = 'react-datepicker__month-container']//div[contains(@class,'react-datepicker__day react-datepicker__day')][contains (@aria-label, '"
						+ calMonth + "')]"));

		// Loop will rotate till expected date not found.
		for (WebElement currentDay : monthDays) {
			// Select the date from date picker when condition match.
			if (currentDay.getText().equals(date)) {
				currentDay.click();
				break;
			}
		}
	}

	/**
	 * Finds and selects the given dropdown item within its parent WebElement
	 * 
	 * @param element      - the parent dropdown
	 * @param dropdownItem - the item being selected in the dropdown
	 */
	public static void selectDropdownItem(WebElement element, String dropdownItem) {
		selectSubItemInElement(element, dropdownItem);
	}

	/**
	 * Generic method to find and select a sub item in a web element
	 * 
	 * @param element - the parent element with sub items
	 * @param subItem - the item being selected
	 */
	private static void selectSubItemInElement(WebElement element, String subItem) {
		Select selectableElement = new Select(element);

		selectableElement.selectByVisibleText(subItem);
	}

	/**
	 * Reads the current selected sub item in a dropdown/list
	 * 
	 * @return String
	 */
	public static String readSelectedSubItem(WebElement element) {
		AutomationHelper.printMethodName();
		List<String> selectedList = readSelectedSubItems(element);

		int i = 0;
		do {
			if (selectedList.size() == 0) {
				AutomationHelper.waitSeconds(1);
				i++;
			}
		} while (selectedList.size() == 0 && i < 5);

		if (selectedList.size() > 1) {
			throw new WrongMethodTypeException(
					"Expected one selection but multiple items selected.  Use 'readSelectedSubItems' instead");
		}

		return selectedList.get(0);

	}

	/**
	 * Reads/returns a list of all selected sub items in a dropdown/list
	 * 
	 * @param element
	 * @return List<String>
	 */
	public static List<String> readSelectedSubItems(WebElement element) {
		AutomationHelper.printMethodName();

		Select selectableElement = new Select(element);

		List<String> selectedList = new ArrayList<String>();
		for (WebElement foundSelected : selectableElement.getAllSelectedOptions()) {
			selectedList.add(foundSelected.getText());
		}
		return selectedList;
	}

	/**
	 * Method to select multiple list items in a list box, as passed in by a
	 * String[];
	 * 
	 * @param listBox
	 * @param itemsToSelect
	 */
	public static void selectListBoxItems(WebElement listBox, String[] itemsToSelect) {

		Select listBoxSelect = new Select(listBox);

		if (itemsToSelect.length > 0) {

			for (String currentElement : itemsToSelect) {
				listBoxSelect.selectByVisibleText(currentElement);
			}
		} else {
			throw new ArrayIndexOutOfBoundsException("There were no items in the items in the itemsToSelect String[]");
		}
	}

	/**
	 * Method to select a single list item in a list box.
	 * 
	 * @param listBox
	 * @param itemToSelect
	 */
	public static void selectListBoxItems(WebElement listBox, String itemToSelect) {
		Select listBoxSelect = new Select(listBox);
		listBoxSelect.selectByVisibleText(itemToSelect);
	}

	public static void adjustWaitTimeToVeryShort() {
		setTimeout(driver, MICRO_TIMEOUT_MILLIS );
	}
	
	public static void adjustWaitTimeToShort() {
		setTimeout(driver, SHORT_TIMEOUT);
	}

	public static void adjustWaitTimeToNormal() {
		setTimeout(driver, NORMAL_TIMEOUT);

	}

	/**
	 * Basic @AfterClass method for staging a test
	 */
	public static void finishTest() {
		AutomationHelper.printClassName();
		AutomationHelper.printMethodName();

		if (driver != null) {
			if (!driver.toString().contains("(null)")) {
				driver.quit();
			}
		}
	}

	/**
	 * Converts all text in a list of web elements to a String list
	 * 
	 * @param List<WebElement>
	 * @return List<String>
	 */
	public static List<String> getStringListFromWebElementList(List<WebElement> list) {
		List<String> stringList = new ArrayList<String>();

		// have to cycle through each element looking for text. Ignore blank
		// elements
		for (WebElement element : list) {
			if (!element.getText().trim().isEmpty()) {
				stringList.add(element.getText().replaceAll("\n", " ").trim());
			}
		}

		return stringList;
	}

	/**
	 * Get all text in a list of web elements as a string
	 * 
	 * @param List<WebElement>
	 * @return String
	 */
	public static String getTextFromWebElementList(List<WebElement> list) {
		List<String> stringList = getStringListFromWebElementList(list);

		return String.join(" ", stringList);
	}

	/**
	 * Method to return the text of a given element. If the default Selenium
	 * getText() method doesn't work, we attempt to get the "value" attribute of the
	 * element.
	 * 
	 * @param element
	 * @return String
	 */
	public static String getText(WebElement element) {
		String text = null;

		text = element.getText();

		// Sometimes the getText() method doesn't pull back a value. When this happens,
		// lets try the .value property of the object
		if (text.equals("")) {
			text = element.getAttribute("value");
		}
		return text;
	}

	/**
	 * Take an element and move it to another element
	 * 
	 * @param source
	 * @param destination
	 */
	public static void dragAndDrop(WebElement source, WebElement destination) {
		// more reliable than .dragAndDrop from Actions
		Actions builder = new Actions(driver);

		builder.clickAndHold(source).moveToElement(destination).perform();
		builder.release(destination).perform();
	}

	/**
	 * Right-click on a specified web element
	 * 
	 * @param element
	 */
	public static void rightClick(WebElement element) {
		Actions builder = new Actions(driver);
		builder.moveToElement(element);
		builder.contextClick(element).perform();
	}

	/**
	 * Presses the tab key using a robot. Helps with focus.
	 */
	public static void tab() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			AutomationHelper.waitMillis(500);
			robot.keyRelease(KeyEvent.VK_TAB);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks to see if a web element exists on the page
	 * 
	 * 
	 * @param webElementToCheck - List<WebElement> - must be done this way to keep
	 *                          from nullreferenceexception from being thrown when
	 *                          element not found
	 * @return boolean - true = web element exists | false = web element does not
	 *         exist
	 */
	public static boolean isWebElementOnPage(List<WebElement> webElementToCheck) {
		boolean elementExists = false;

		if (webElementToCheck != null && webElementToCheck.size() > 0) {
			elementExists = true;
		}

		return elementExists;
	}

	/**
	 * Method to ensure that a valid date is returned in the expected format. This
	 * method can throw a Parse Exception if dates are not recognizable.
	 * 
	 * @param dateFormat - e.g. MM/dd/yyyy or MM-dd-yyyy
	 * @param dateValue  - e.g. 12/15/2021
	 * @return true or false
	 */
	public static boolean isDateFormatValid(String dateFormat, String dateValue) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			date = sdf.parse(dateValue);
			if (!dateValue.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date != null;
	}

	/**
	 * Method to wait for a file to download before proceeding. It ensures that the
	 * file name passed in will be found in the filePathForSave directory static
	 * variable in this page class (SW2Config.java). The method will wait two
	 * minutes before throwing an exception.
	 * 
	 * @param fileName - the file name, e.g. Demo.pdf
	 */
	public static void waitForFileDownload(String fileName) {
		AutomationHelper.printMethodName();

		Path filePath = Paths.get(DEFAULT_FILE_PATH_FOR_SAVING, fileName);
		await().atMost(2, MINUTES).ignoreExceptions().until(() -> filePath.toFile().exists());

	}

}

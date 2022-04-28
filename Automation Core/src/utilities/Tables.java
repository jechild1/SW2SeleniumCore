package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.CannotProceedException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import configuration.TestConfig;

public class Tables extends TestConfig {

	public WebElement table;

	/**
	 * Initialize Tables with table WebElement
	 * 
	 * @param tableReference
	 */
	public Tables(WebElement tableReference) {
		table = tableReference;
	}

	/**
	 * Clicks a link in table row with that matches value 1 and value 2, and
	 * contains the passed in Link Text.
	 * 
	 * @param rowValueOne
	 * @param linkText
	 */
	public void clickLinkInRow(String rowValueOne, String linkText) {
		Reporter.log("Beginning link click for row " + rowValueOne + " and link title " + linkText + ".", true);

		WebElement myRow = null;

		// Create a list of table rows
		List<WebElement> tableRows = table.findElements(By.tagName("tr"));

		// Loop through all the table rows to find the row containing the value
		// 1 and value 2 combination.
		for (WebElement currentRow : tableRows) {
			if (currentRow.getText().trim().contains(rowValueOne)) {
				myRow = currentRow;
				break;
			}
		}

		// If we did not find a row, we should throw an exception and not
		// process further.
		if (myRow == null) {
			throw new RuntimeException("Cannot click link. There are no rows found in the table for " + rowValueOne);
		}

		WebElement link = myRow.findElement(By.linkText(linkText));
		link.click();

		Reporter.log("Link " + linkText + " clicked for row " + rowValueOne + ".", true);
		Reporter.log("", true);
	}

	/**
	 * Clicks a link in table row with that matches value 1 and value 2, and
	 * contains the passed in Link Text.
	 * 
	 * @param rowValueOne
	 * @param rowValueTwo
	 * @param linkText
	 */
	public void clickLinkInRow(String rowValueOne, String rowValueTwo, String linkText) {
		Reporter.log(
				"Beginning link click for row " + rowValueOne + " " + rowValueTwo + " and link title " + linkText + ".",
				true);

		// Take the two row values and concatenate them.
		String myConcatenatedString = rowValueOne.trim() + " " + rowValueTwo.trim();
		WebElement myRow = null;

		// Create a list of table rows
		List<WebElement> tableRows = table.findElements(By.tagName("tr"));

		// Loop through all the table rows to find the row containing the value
		// 1 and value 2 combination.
		for (WebElement currentRow : tableRows) {
			if (currentRow.getText().trim().contains(myConcatenatedString)) {
				myRow = currentRow;
				break;
			}
		}

		// If we did not find a row, we should throw an exception and not
		// process further.
		if (myRow == null) {
			throw new RuntimeException(
					"Cannot click link. There are no rows found in the table for " + myConcatenatedString);
		}

		WebElement link = myRow.findElement(By.linkText(linkText));
		link.click();

		Reporter.log("Link " + linkText + " clicked for row " + rowValueOne + " " + rowValueTwo + ".", true);
		Reporter.log("", true);
	}

	/**
	 * Clicks a link in table row with that matches value 1, value 2, and value 3
	 * and contains the passed in Link Text.
	 * 
	 * @param rowValueOne
	 * @param rowValueTwo
	 * @param rowValueThree
	 * @param linkText
	 */
	public void clickLinkInRow(String rowValueOne, String rowValueTwo, String rowValueThree, String linkText) {
		Reporter.log("Beginning link click for row " + rowValueOne + " " + rowValueTwo + " " + rowValueThree
				+ " and link title " + linkText + ".", true);

		// Take the three row values and concatenate them.
		String myConcatenatedString = rowValueOne.trim() + " " + rowValueTwo.trim() + " " + rowValueThree.trim();
		WebElement myRow = null;

		// Create a list of table rows
		List<WebElement> tableRows = table.findElements(By.tagName("tr"));

		// Loop through all the table rows to find the row containing the value
		// 1, value 2, & value 3 combination.
		for (WebElement currentRow : tableRows) {
			if (currentRow.getText().trim().contains(myConcatenatedString)) {
				myRow = currentRow;
				break;
			}
		}

		// If we did not find a row, we should throw an exception and not
		// process further.
		if (myRow == null) {
			throw new RuntimeException(
					"Cannot click link. There are no rows found in the table for " + myConcatenatedString);
		}

		WebElement link = myRow.findElement(By.linkText(linkText));
		link.click();

		Reporter.log(
				"Link " + linkText + " clicked for row " + rowValueOne + " " + rowValueTwo + " " + rowValueThree + ".",
				true);
		Reporter.log("", true);
	}

	/**
	 * Clicks a link in table row that matches the passed in link text.
	 * 
	 * @param tableRow
	 * @param linkText
	 */
	public void clickLinkInRow(WebElement tableRow, String linkText) {
		Reporter.log("Beginning link click for row.", true);

		// Create a list of all of the links that are children of the Table Row
		List<WebElement> childrenLinks = tableRow.findElements(By.xpath("descendant::a"));

		if (childrenLinks.size() < 1) {
			throw new NoSuchElementException("Cannot find Any links in the table row.");
		}

		// Found flag
		boolean rowFound = false;
		for (WebElement currentLink : childrenLinks) {
			if (currentLink.getText().equals(linkText)) {
				currentLink.click();
				AutomationHelper.waitForPageToLoad(NORMAL_TIMEOUT);
				rowFound = true;
				break;
			}
		}

		if (!rowFound) {
			throw new NoSuchElementException("Cannot find a link in the table row with the text '" + linkText + "'.");
		}

	}

	/**
	 * Clicks a link in specified row column by text
	 * 
	 * NOTE: use when multiple links are in one column
	 * 
	 * @param primaryColumnHeader
	 * @param primaryColumnValue
	 * @param columnToClick
	 * @param linkToClick
	 * @param throwEx
	 */
	public void clickLinkInRow(String primaryColumnHeader, String primaryColumnValue, String columnToClick,
			String linkToClick, boolean throwEx) {

		Reporter.log("Beginning click for '" + linkToClick + "' in '" + columnToClick + "' where primary column is '"
				+ primaryColumnHeader + "' and primary column value is '" + primaryColumnValue + ".", true);

		// Get CellIndex for Primary column Header
		int primaryColIndex = getColumnIndex(primaryColumnHeader, throwEx);
		// Get CellIndex for Column to Read
		int colToReadCellIndex = getColumnIndex(columnToClick, throwEx);
		// Get the rowIndex for the row in the primary column.
		int rowIndex = getRowIndex(primaryColIndex, primaryColumnValue, throwEx);

		WebElement cell = getCell(rowIndex, colToReadCellIndex, throwEx);

		cell.findElement(By.linkText(linkToClick)).click();
	}

	/**
	 * Clicks a button in table row with that matches value 1 and value 2, and
	 * contains the passed in Button Text.
	 * 
	 * @param rowValueOne
	 * @param rowValueTwo
	 * @param buttonText
	 */
	public void clickButtonInRow(String rowValueOne, String rowValueTwo, String buttonText) {
		AutomationHelper.printMethodName();

		// Take the two row values and concatenate them.
		String myConcatenatedString = rowValueOne.trim() + " " + rowValueTwo.trim();
		WebElement myRow = null;

		// Create a list of table rows
		List<WebElement> tableRows = table.findElements(By.tagName("tr"));

		// Loop through all the table rows to find the row containing the value
		// 1 and value 2 combination.
		for (WebElement currentRow : tableRows) {
			if (currentRow.getText().trim().contains(myConcatenatedString)) {
				myRow = currentRow;
				break;
			}
		}

		// If we did not find a row, we should throw an exception and not
		// process further.
		if (myRow == null) {
			throw new RuntimeException(
					"Cannot click button. There are no rows found in the table for " + myConcatenatedString);
		}

		WebElement link = myRow.findElement(By.tagName("button"));
		link.click();

		// Allow a moment for the page to load.
		//TODO - This can perhaps be improved upon.
		AutomationHelper.waitSeconds(2);
	}

//	public boolean isRowInTable(String... rowValues) {
//		// Boolean to indicate the row is found
//		boolean found = false;
//		int pagedTables = 0;
//
//		String myConcatenatedString = "";
//		for (String currentRowValue : rowValues) {
//			// Space necessary at the end in order to match each one.
//			myConcatenatedString += currentRowValue + " ";
//		}
//
//		myConcatenatedString = ".*" + AutomationHelper.escapeStringForRegEx(myConcatenatedString).trim() + ".*";
//
//		Reporter.log("Beginning isRowInTable search for " + myConcatenatedString + ".", true);
//
//		// If not already on the first page, ensure we go back to the first page
//		while (isSkipToPreviousNavigationPresent()) {
//			clickSkipToPreviousNavigationLink();
//		}
//
//		int finalCounter = 0;
//		// Loop through each row and look for the string we pass in.
//		while (!found && finalCounter <= 1) {
//
//			// Create a list of table rows
//			List<WebElement> tableRows = table.findElements(By.tagName("tr"));
//
//			// Increment the number of times we have checked a table for the value
//			pagedTables += 1;
//			int rowIterator = 0;
//
//			for (WebElement currentRow : tableRows) {
//
//				// Consider a .equals instead of .contains.
//				String text = currentRow.getText().replace("\n", "").trim();
//				System.out.println("Performing search on page " + pagedTables + " for row text: " + text);
//				if (text.matches(myConcatenatedString)) {
//					found = true;
//					System.out.println("MATCH FOUND IN ROW " + rowIterator + " OF TABLE PAGE " + pagedTables);
//					break;
//				}
//				rowIterator++;
//			}
//
//			if (!found) {
//				System.out.println(
//						"MATCH NOT FOUND ON PAGE " + pagedTables + " FOR VALUE " + "'" + myConcatenatedString + "'");
//
//				if (isSkipToNextNavigationPresent()) {
//					clickSkipToNextNavigationLink();
//				}
//
//				if (!isSkipToNextNavigationPresent()) {
//					finalCounter++;
//				}
//
//			}
//
//		}
//
//		Reporter.log("Table search completed for " + myConcatenatedString + " and Row Found = " + found
//				+ " after searching through " + pagedTables + "pages.");
//		Reporter.log("", true);
//
//		return found;
//
//	}

	/**
	 * Checks for the presence of a row by primary column value.
	 * 
	 * @param primaryColumnHeader
	 * @param primaryColumnValue
	 * 
	 * @return boolean - returns true if found; false if not found.
	 */
	public boolean isRowInTableByValue(String primaryColumnHeader, String primaryColumnValue) {

		Reporter.log(String.format("Beginning isRowInTable search for '%s' in column '%s'.", primaryColumnValue,
				primaryColumnHeader), true);

		// Get CellIndex for Primary column Header
		int primaryColIndex = getColumnIndex(primaryColumnHeader, false);
		// Get the rowIndex for the row in the primary column.
		int rowIndex = getRowIndex(primaryColIndex, primaryColumnValue, false);

		// return true if rowindex is greater than -1 (not found)
		return rowIndex > -1;
	}

	/**
	 * Returns whether a cell in a specified table row is selected or not.
	 * 
	 * @param primaryColumnHeader
	 * @param primaryColumnValue
	 * @param columnToRead
	 * @param throwEx
	 * @return boolean - true = checked | false = not checked
	 */
	public boolean isTableRowValueSelected(String primaryColumnHeader, String primaryColumnValue, String columnToRead,
			boolean throwEx) {

		Reporter.log("Beginning read for '" + columnToRead + "' where primary column is '" + primaryColumnHeader
				+ "' and primary column value is '" + primaryColumnValue + ".", true);

		// Get CellIndex for Primary column Header
		int primaryColIndex = getColumnIndex(primaryColumnHeader, throwEx);
		// Get CellIndex for Column to Read
		int colToReadCellIndex = getColumnIndex(columnToRead, throwEx);
		// Get the rowIndex for the row in the primary column.
		int rowIndex = getRowIndex(primaryColIndex, primaryColumnValue, throwEx);

		WebElement cell = getCell(rowIndex, colToReadCellIndex, throwEx);

		return cell == null ? null : cell.findElement(By.xpath(".//input")).isSelected();
	}

//	/**
//	 * Returns a cell value from a table using the column specified in the row that
//	 * matches the first two criteria (Column/Value pair).
//	 * 
//	 * @param primaryColumnHeader
//	 * @param primaryColumnValue
//	 * @param columnToRead
//	 * @param throwEx
//	 * @return String - the value of a cell
//	 */
//	public String readTableRowValue(String primaryColumnHeader, String primaryColumnValue, String columnToRead,
//			boolean throwEx) {
//
//		Reporter.log("Beginning read for column '" + columnToRead + "' where primary column header is '"
//				+ primaryColumnHeader + "' and primary column value is '" + primaryColumnValue + "' on page "
//				+ readTablePageNumber() + ".", true);
//
//		// Get CellIndex for Primary column Header
//		int primaryColIndex = getColumnIndex(primaryColumnHeader, throwEx);
//		// Get CellIndex for Column to Read
//		int colToReadCellIndex = getColumnIndex(columnToRead, throwEx);
//		// Get the rowIndex for the row in the primary column.
//		int rowIndex = getRowIndex(primaryColIndex, primaryColumnValue, throwEx);
//
//		WebElement cell = getCell(rowIndex, colToReadCellIndex, throwEx);
//
//		return cell == null ? null : cell.getText();
//	}

	/**
	 * <b>NOTE: Current this method is returning a cell, despite the name. The TR is
	 * not clickable at the moment. </b><br>
	 * Returns a specific Row in a table based on the primary column and primary
	 * column value.
	 * 
	 * @param primaryColumnName
	 * @param primaryColumnValue
	 * @param throwEx
	 * @return WebElement
	 */
	protected WebElement getRow(String primaryColumnName, String primaryColumnValue, boolean throwEx) {

		// Final object to return
		WebElement rowToClick = null;

		// To hold total row count
		int rowsFound = 0;

		// Find all the table rows within the table
		List<WebElement> tableRowsList = table.findElements(By.tagName("tr"));

		// Get the column index of the primary column name.
		int primaryColIndex = getColumnIndex(primaryColumnName, true);

		// If we do not find a column index in the previous step.
		if (primaryColIndex == -1) {
			throw new NoSuchElementException("There is not a primary column with the text " + primaryColumnName);
		}

		// Cycle through each row in the current table
		for (WebElement currentRow : tableRowsList) {

			// For each row, check if the row contains the primary column value
			if (currentRow.getText().contains(primaryColumnValue)) {

				// Now narrow the scope. Within the row, get the cell that contains the row
				// index and the primary column index. If it equals the value we passed in, then
				// assign that object to the returnable object.

				int rowIndex = getRowIndex(primaryColIndex, primaryColumnValue, throwEx);
				WebElement cellWithText = getCell(rowIndex, primaryColIndex, true);

				if (cellWithText.getText().equals(primaryColumnValue)) {

					// TODO This is where the issue is. The TR's are not clickable. Right now, I'll
					// pass the CELL back and not the row. If this is fixed, the next line shoudl be
					// rowToClick = currentRowl

					rowToClick = cellWithText;
					rowsFound++;
				}

			}
		}

		// If we found more than one row, we need to throw an exception to let the user
		// know they need unique data.
		if (rowsFound > 1) {
			throw new NoSuchElementException("There are too many rows found with the primary column name of "
					+ primaryColumnName + " and primary row value of " + primaryColumnValue);
		}

		// Do not throw an exception here. We want to return a null so that pagination
		// can exist inside the project, e.g. SW2Tables.
		
//		// IF we didn't find a row, rowToClick is null and we need to throw an
//		// exception.
//		if (rowToClick == null) {
//			throw new ElementNotInteractableException("There is not a record found for primary column '" + primaryColumnName
//					+ "' and primary column value of '" + primaryColumnValue + "'.");
//		}

		return rowToClick;
	}

	/**
	 * Returns a specific cell in a table based on the row and column index passed
	 * in
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return WebElement - the cell requested
	 * @exception NotFoundException
	 */
	protected WebElement getCell(int rowIndex, int columnIndex, boolean throwEx) {
		// Find all the table rows within the table
		List<WebElement> tableRowsList = table.findElements(By.tagName("tr"));
		WebElement aquiredCell = null;

		// If exceptions are turned off, do not run the code
		if (columnIndex != -1 && rowIndex != -1) {

			// Find the ROW where the primary column value is located
			WebElement aquiredRow = null;
			for (WebElement currentRow : tableRowsList) {
				if (Integer.valueOf(currentRow.getAttribute("rowIndex")) == rowIndex) {
					aquiredRow = currentRow;
					break;
				}
			}

			// Create a list of cells for the row found above;
			List<WebElement> cellListForRow = aquiredRow.findElements(By.tagName("td"));

			// Find the cell in the row where the cell index matches the
			// colToReadCellIndex
			for (WebElement currentCell : cellListForRow) {
				if (Integer.valueOf(currentCell.getAttribute("cellIndex")) == columnIndex) {
					aquiredCell = currentCell;
					break;
				}
			}

		} else if (throwEx) {
			throw new NotFoundException("The cell requested could not be found in the table");
		}

		return aquiredCell;
	}

	// public static boolean readActiveStatusFromRow(WebElement table, String
	// rowValueOne, String rowValueTwo) {
	// Reporter.log("Beginning Active Status checkbox status read for row " +
	// rowValueOne + " " + rowValueTwo + ".",
	// true);
	//
	// boolean checkboxStatus;
	// // Take the two row values and concatenate them.
	// String myConcatenatedString = rowValueOne.trim() + " " +
	// rowValueTwo.trim();
	// WebElement myRow = null;
	//
	// // Create a list of all of the rows in the table.
	// List<WebElement> tableRows = table.findElements(By.tagName("tr"));
	//
	// // Loop through all the table rows to find the row containing the value
	// // 1 and value 2 combination.
	// for (WebElement currentRow : tableRows) {
	// if (currentRow.getText().trim().contains(myConcatenatedString)) {
	// myRow = currentRow;
	// break;
	// }
	// }
	//
	// // If we did not find a row, we should throw an exception and not
	// // process further.
	// if (myRow == null) {
	// throw new RuntimeException("Cannot perform Active checkbox read. There
	// are no
	// rows found in the table for "
	// + myConcatenatedString);
	// }
	//
	// // get the status of the checkbox in the row we found.
	// WebElement checkbox = myRow.findElement(By.id("item_ActiveFlag"));
	// checkboxStatus = Boolean.valueOf(checkbox.getAttribute("checked"));
	//
	// Reporter.log("Active Checkbox status for " + rowValueOne + " " +
	// rowValueTwo
	// + " is: " + checkboxStatus + ".",
	// true);
	// Reporter.log("", true);
	//
	// return checkboxStatus;
	// }

	/**
	 * This method returns a column index the passed in table/column name
	 * combination.
	 * 
	 * @param columnName
	 * @param throwEx    - if table doesn't have THEAD objects
	 * @return int - the Column Index of a column (0 based)
	 */
	protected int getColumnIndex(String columnName, boolean throwEx) {
		int columnIndex = -1;
		// Get all the THEADS in a given table
		List<WebElement> tHeads = table.findElements(By.tagName("th"));

		if (tHeads.size() > 0) {

			for (WebElement x : tHeads) {

				if (columnName.equalsIgnoreCase(AutomationHelper.removeMarkupFromString(x.getText()))) {
					columnIndex = Integer.valueOf(x.getAttribute("cellIndex"));
					break;
				}
			}
		} else {
			if (throwEx) {
				throw new RuntimeException("The table does not have any THEAD objects");
			}
		}
		return columnIndex;
	}

	/**
	 * This method accepts a table, and returns the first row in that tables that IS
	 * NOT the header row.
	 * 
	 * @return WebElement
	 */
	public WebElement getFirstRowInTable() {
		return table.findElement(By.xpath(".//tr[descendant::td][1]"));
	}

	/**
	 * Returns a list of table rows for the visible table.
	 * <p>
	 * Note: This does not handle paging, but displays only what is on the current
	 * displayed page.
	 * <p>
	 * Note: This methods DOES NOT COUNT the header row.
	 * 
	 * @param table
	 * 
	 * @return List <WebElement> - Table Rows
	 */
	public List<WebElement> getTableRowsForVisibleTable() {
		List<WebElement> currentPageTableRows;

		currentPageTableRows = table.findElements(By.xpath(".//tr[descendant::td]"));

		return currentPageTableRows;
	}

	/**
	 * Returns a list of table rows for ALL table items. This takes care of paging
	 * and will go to the end of a table to grab ALL the rows. After it is finished,
	 * it returns to the first row.
	 * <p>
	 * Note: This methods DOES NOT COUNT the header row.
	 * 
	 * @param driver
	 * @return List <WebElement> - Table Rows
	 */
	public List<WebElement> getTableRowsForPagingTable(WebDriver driver) {

		driver.manage().timeouts().implicitlyWait(SHORT_TIMEOUT, TimeUnit.SECONDS);

		// Create an initial list of table rows
		List<WebElement> totalTableRows = table.findElements(By.xpath("//tr[descendant::td]"));

		System.out.println("Total table row is now: " + totalTableRows.size());

		// // Try to find the >> arrow in the pagination feature
		WebElement skipToNextEnabled;

		do {
			skipToNextEnabled = null;

			try {
				skipToNextEnabled = driver.findElement(By.xpath("//li[@class = 'PagedList-skipToNext']//a"));
			} catch (NoSuchElementException e) {
			}

			if (skipToNextEnabled != null) {
				// If we find a reference to the button that is not disabled,
				// then click it

				skipToNextEnabled.click();
				AutomationHelper.waitForPageToLoad(10);

				// Add the table rows on this page to the List
				List<WebElement> currentTableRows = table.findElements(By.xpath("//tr[descendant::td]"));

				for (WebElement x : currentTableRows) {
					totalTableRows.add(x);
				}
			}

		} while (skipToNextEnabled != null);

		// At last, we want to return to the main page of the table.
		// Attempt to click the << << link. This link is present where there are
		// many records in a table.
		// If it is not there, attempt << until it is disabled.
		try {
			// Try to click << << if it exists
			WebElement pageAll = null;
			pageAll = driver.findElement(By.xpath("//li[@class='PagedList-skipToFirst']//a"));
			pageAll.click();

		} catch (NoSuchElementException e) {
		}

		// If << << Does not exist, click << until it is disabled, letting us
		// know we are on the first page.
		WebElement skipToPrevious = null;
		do {
			try {
				// Set SkipToPrevious back to null for the next click
				skipToPrevious = null;

				// Attempt the reference here. If it finds it, it will click. If
				// not, it will catch the exception
				skipToPrevious = driver.findElement(By.xpath("//li[@class='PagedList-skipToPrevious']//a"));
				skipToPrevious.click();

			} catch (NoSuchElementException e) {
			}

		} while (skipToPrevious != null);

		driver.manage().timeouts().implicitlyWait(NORMAL_TIMEOUT, TimeUnit.SECONDS);

		return totalTableRows;
	}

	/**
	 * Returns the number of rows in a given table. Note: this does not contain the
	 * Header Row (THEAD).
	 * 
	 * @return int
	 */
	public int getTableRowCount() {
		int rowCount = getTableRowsForPagingTable(driver).size();
		if (rowCount == 0) {
			throw new RuntimeException("There are no rows in the table.");
		}
		return rowCount;
	}

	/**
	 * This method takes a table, Primary Column Header (THEAD), and a value in that
	 * column and returns a row index for the passed in value.
	 * 
	 * @param primaryColumnHeader
	 * @param primaryColumnValue  (regex)
	 * @param throwEx
	 * @return int rowIndex
	 */
	protected int getRowIndex(int primaryColumnHeaderIndex, String primaryColumnValue, boolean throwEx) {
		// Variable to hold the row index.
		int rowIndex = -1;

		// Escape any special characters in the primaryColumnValue
		primaryColumnValue = AutomationHelper.escapeStringForRegEx(primaryColumnValue);

		// Get a list of ALL of the table cells
		AutomationHelper.adjustWaitTimeToShort();
		List<WebElement> tableCells = table.findElements(By.tagName("td"));
		AutomationHelper.adjustWaitTimeToNormal();

		// Get a list of all of the table cells that have a cellIndex
		// corresponding to the primary column header.
		List<WebElement> cellsInPrimaryCol = new ArrayList<WebElement>();

		for (WebElement currentCell : tableCells) {
			if (Integer.valueOf(currentCell.getAttribute("cellIndex")) == primaryColumnHeaderIndex) {
				cellsInPrimaryCol.add(currentCell);
			}
		}

		// Creating a list for the final cell allows us to check for multiples
		// and throw correct exceptions, if needed.
		List<WebElement> finalCellList = new ArrayList<WebElement>();

		// Loop through each cell in the primary column, and look for the
		// primary column value, then take the rowIndex for that cell.
		for (WebElement currentCell : cellsInPrimaryCol) {

			// The method below used to use .equals, but changed to .matches
			// so that dates such as 12/15/2018 09:56 can be accounted for
			// without using time (send wild card)
			if (currentCell.getText().trim().matches(primaryColumnValue.trim())) {
				finalCellList.add(currentCell);
			}
		}

		// Throw an exception if we find more than one cell with the same value.
		if (finalCellList.size() > 1) {
			throw new RuntimeException("There are multiple cells in column index " + primaryColumnHeaderIndex
					+ " with the value " + primaryColumnValue + ". Cannot determine a unique value.");
		}

		// Throw an exception if we do not find any cells the user is looking
		// for.
		if (throwEx) {
			if (finalCellList.size() == 0) {
				throw new RuntimeException("There are no cells in the column index " + primaryColumnHeaderIndex
						+ " that match the value " + primaryColumnValue + " that was passed in. Cannot find a cell. ");
			}
		}

		// If we don't wrap this in an IF, we can throw null pointer if
		// exceptions are turned off.
		if (finalCellList.size() == 1) {

			// Because a cell does not have a rowIndex, get the parent row
			WebElement parentRow = finalCellList.get(0).findElement(By.xpath(".."));

			// Store the row Index from the cell parent row in a variable to
			// return.
			rowIndex = Integer.valueOf(parentRow.getAttribute("rowIndex"));
		}

		return rowIndex;
	}

	/**
	 * This method takes a table, Primary Column Header (THEAD), Secondary Column
	 * Header (THEAD), and a values in both columns and returns a row index for the
	 * passed in value.
	 * 
	 * @param primaryColumnHeader
	 * @param primaryColumnValue
	 * @param secondaryColumnHeaderIndex
	 * @param secondaryColumnValue
	 * @param throwEx
	 * @return int rowIndex
	 */
	protected int getRowIndex(int primaryColumnHeaderIndex, String primaryColumnValue, int secondaryColumnHeaderIndex,
			String secondaryColumnValue, boolean throwEx) {
		// Variable to hold the row index.
		int rowIndex = -1;

		// Get a list of ALL of the table cells
		AutomationHelper.adjustWaitTimeToShort();
		List<WebElement> tableCells = table.findElements(By.tagName("td"));
		AutomationHelper.adjustWaitTimeToNormal();

		// Get a list of all of the table cells that have a cellIndex
		// corresponding to the primary column header.
		List<WebElement> cellsInPrimaryCol = new ArrayList<WebElement>();
		List<WebElement> cellsInSecondaryCol = new ArrayList<WebElement>();

		for (WebElement currentCell : tableCells) {
			if (Integer.valueOf(currentCell.getAttribute("cellIndex")) == primaryColumnHeaderIndex) {
				cellsInPrimaryCol.add(currentCell);
			} else if (Integer.valueOf(currentCell.getAttribute("cellIndex")) == secondaryColumnHeaderIndex) {
				cellsInSecondaryCol.add(currentCell);
			}
		}

		if (cellsInPrimaryCol.size() != cellsInSecondaryCol.size()) {
			if (throwEx) {
				throw new NotFoundException(String.format(
						"Unable to find accurate row list using primary header index %d and secondary header index %d.",
						primaryColumnHeaderIndex, secondaryColumnHeaderIndex));
			} else {
				return rowIndex;
			}
		}

		// Creating a list for the final cell allows us to check for multiples
		// and throw correct exceptions, if needed.
		List<WebElement> finalCellList = new ArrayList<WebElement>();

		// Loop through each cell in the primary column, and look for the
		// primary column value, then take the rowIndex for that cell.
		for (int i = 0; i < cellsInPrimaryCol.size(); i++) {
			String currentPrimaryCell = cellsInPrimaryCol.get(i).getText().trim();
			String currentSecondaryCell = cellsInSecondaryCol.get(i).getText().trim();

			// The method below used to use .equals, but changed to .startsWith
			// so that dates such as 12/15/2018 09:56 can be accounted for
			// without using time.
			if (currentPrimaryCell.startsWith(primaryColumnValue.trim())
					&& currentSecondaryCell.startsWith(secondaryColumnValue.trim())) {
				finalCellList.add(cellsInPrimaryCol.get(i));
			}
		}

		// Throw an exception if we find more than one cell with the same value.
		if (finalCellList.size() > 1) {
			throw new RuntimeException(String.format(
					"There are multiple cells in primary column index %d with the value %s and secondary column index %d with the value %s. Cannot determine a unique value.",
					primaryColumnHeaderIndex, primaryColumnValue, secondaryColumnHeaderIndex, secondaryColumnValue));
		}

		// Throw an exception if we do not find any cells the user is looking
		// for.
		if (throwEx) {
			if (finalCellList.size() == 0) {
				throw new RuntimeException(String.format(
						"There are no cells in the column index %d that match the value %s and secondary column index %d with the value %s that was passed in. Cannot find a cell.",
						primaryColumnHeaderIndex, primaryColumnValue, secondaryColumnHeaderIndex,
						secondaryColumnValue));
			}
		}

		// If we don't wrap this in an IF, we can throw null pointer if
		// exceptions are turned off.
		if (finalCellList.size() == 1) {

			// Because a cell does not have a rowIndex, get the parent row
			WebElement parentRow = finalCellList.get(0).findElement(By.xpath(".."));

			// Store the row Index from the cell parent row in a variable to
			// return.
			rowIndex = Integer.valueOf(parentRow.getAttribute("rowIndex"));
		}

		return rowIndex;
	}

	/**
	 * Method to extract the text from a a cell for given row index and column
	 * 
	 * @param rowIndex
	 * @param columnName
	 * @return String
	 */
	public String getCellText(int rowIndex, String columnName) {

		boolean cellFound = false;
		String cellText = null;

		// Grab all the CELLS in the table
		List<WebElement> tableCells = table.findElements(By.tagName("td"));

		// Loop through the cells to find the one that matches the row / column
		// name
		// pair
		for (WebElement currentCell : tableCells) {

			// First see that the cell ROW index matches what we passed in.
			// Because a cell does not have a rowIndex, get the parent row
			WebElement parentRow = currentCell.findElement(By.xpath(".."));
			if ((Integer.valueOf(parentRow.getAttribute("rowIndex"))) == rowIndex) {

				// If the ROW matches, then see if the COLUMN matches
				if (Integer.valueOf(currentCell.getAttribute("cellIndex")) == getColumnIndex(columnName, true)) {

					cellText = currentCell.getText();
					cellFound = true;
					break;

				}

			}

		}

		if (!cellFound) {
			try {
				throw new CannotProceedException("Cant find the cell");
			} catch (CannotProceedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cellText;

	}
	
	/**
	 * Method to return all of the table rows within a given table. This does not count THEAD objects.
	 * 
	 * @return int
	 */
	public int getRowCount() {

		// Get a list of all all the TD's in the table
		return table.findElements(By.xpath(".//tr[descendant::td]")).size();

	}

//	/**
//	 * Reads the current table page number that is active (highlighted).
//	 * 
//	 * @return int
//	 */
//	public static String readTablePageNumber() {
//		AutomationHelper.setTimeout(driver, SHORT_TIMEOUT);
//		WebElement currentPage = null;
//		try {
//			currentPage = driver.findElement(By.xpath("//ul[@class='pagination']//li[@class='active']//a"));
//		} catch (NoSuchElementException e) {
//	
//		}
//		;
//		AutomationHelper.setTimeout(driver, NORMAL_TIMEOUT);
//	
//		// This will throw a number format exception if there are no numbers.
//		String pageNumber = null;
//		if (currentPage != null) {
//	
//			pageNumber = currentPage.getText();
//			// If the page number isn't a digit, then return a message saying so.
//			if (!pageNumber.matches("\\d+")) {
//				pageNumber = "(no table paging)";
//	
//			}
//		}
//		// Ensure pagination is a digit. If not, return message
//		return (pageNumber == null) ? "No Pagination Table" : pageNumber;
//	
//	}
//
//	/**
//	 * Checks to see if the Skip To Next navigation link (>>) is present.
//	 * 
//	 * @return boolean
//	 */
//	public static boolean isSkipToNextNavigationPresent() {
//		AutomationHelper.printMethodName();
//		boolean exists = false;
//		try {
//
//			AutomationHelper.adjustWaitTimeToShort();
//			driver.findElement(By.xpath("//li[@class = 'PagedList-skipToNext']//a"));
//			AutomationHelper.adjustWaitTimeToNormal();
//
//			exists = true;
//		} catch (NoSuchElementException e) {
//		} catch (TimeoutException e) {
//
//		}
//		return exists;
//	}
//
//	/**
//	 * Checks to see if the Skip To Previous navigation link (<<) is present.
//	 * 
//	 * @return boolean
//	 */
//	public static boolean isSkipToPreviousNavigationPresent() {
//		AutomationHelper.printMethodName();
//		boolean exists = false;
//		try {
//
//			AutomationHelper.adjustWaitTimeToShort();
//			driver.findElement(By.xpath("//li[@class = 'PagedList-skipToPrevious']//a"));
//			AutomationHelper.adjustWaitTimeToNormal();
//
//			exists = true;
//		} catch (NoSuchElementException e) {
//		} catch (TimeoutException e) {
//
//		}
//		return exists;
//	}
//
//	/**
//	 * Returns a reference to the Skip To Next navigation link (>>).
//	 * 
//	 * @param driver
//	 * @return WebElement
//	 */
//	public static WebElement getSkipToNextNavigationLink(WebDriver driver) {
//		return driver.findElement(By.xpath("//li[@class = 'PagedList-skipToNext']//a"));
//	}
//
//	/**
//	 * Returns a reference to the Skip to Previous navigation link (<<).
//	 * 
//	 * @return WebElement
//	 */
//	public static WebElement getSkipToPreviousNavigationLink() {
//		return driver.findElement(By.xpath("//li[@class = 'PagedList-skipToPrevious']//a"));
//	}
//
//	/**
//	 * Clicks the Next navigation link (>>)
//	 */
//	public static void clickSkipToNextNavigationLink() {
//		AutomationHelper.printMethodName();
//		getSkipToNextNavigationLink(driver).click();
//	}
//
//	/**
//	 * Clicks the Previous navigation link (<<)
//	 */
//	public static void clickSkipToPreviousNavigationLink() {
//		AutomationHelper.printMethodName();
//		getSkipToPreviousNavigationLink().click();
//	}
}

package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

public class ExcelDataConfig {
	XSSFWorkbook workBook;
	XSSFSheet workSheet;
	XSSFRow row;
	XSSFCell cell;

	// Capture the file path as it goes into the constructor and store it for
	// other methods.
	String filePath;
	private String columnHeaderKey = "TestDataID";

	// Constructor
	public ExcelDataConfig(String excelPath, String worksheetName) {

		// Capture the file path and store it in a string. Can be used for other
		// methods, but not associated with the constructor.
		filePath = excelPath;

		try {
			// Create a new file path
			File filePath = new File(excelPath);

			// Create workbook object
			workBook = new XSSFWorkbook(filePath);

			// Create a worksheet object
			workSheet = workBook.getSheet(worksheetName);

		} catch (InvalidFormatException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Resets workbook instance
	 */
	public void reset() {
		try {
			workBook = new XSSFWorkbook(filePath);
			workSheet = workBook.getSheet(workSheet.getSheetName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets the columnHeaderKey to a new value rather than default "TestDataID"
	 * 
	 * @param columnHeaderName
	 */
	public void setColumnHeaderKey(String columnHeaderName) {
		if (columnHeaderName == null || columnHeaderName.isEmpty()) {
			throw new IllegalArgumentException("columnHeaderName must not be null or empty");
		}
		this.columnHeaderKey = columnHeaderName;
	}

	/**
	 * Returns the String cell value for a requested column based on a known column
	 * cell value.
	 * 
	 * NOTE: the column header key always defaults to "TestDataID" for where it will
	 * look for the columnHeaderKeyValue. Use setColumnHeaderKey to change this.
	 * 
	 * @param columnHeaderKeyValue
	 * @param columnToRead
	 * @return String
	 */
	public String getData(String columnHeaderKeyValue, String columnToRead) {
		// Only want this to print to the console - but important for debugging
		// purposes
		System.out.println(String.format("Using %s as the column header key.  Attempting to read %s using value %s",
				this.columnHeaderKey, columnToRead, columnHeaderKeyValue));

		int rowIndex = this.getRowIndex(this.columnHeaderKey, columnHeaderKeyValue);
		int columnIndex = this.getColumnIndex(columnToRead);

		return this.getData(rowIndex, columnIndex);
	}

	/**
	 * Returns the String cell value for a requested column based on a known column
	 * row value.
	 * 
	 * 
	 * @param rowIndex
	 * @param columnToRead
	 * @return String
	 */
	public String getData(int rowIndex, String columnToRead) {

		int columnIndex = this.getColumnIndex(columnToRead);

		return this.getData(rowIndex, columnIndex);
	}

	/**
	 * Returns the String value for the row and column number on the given sheet
	 * name.
	 * 
	 * @param sheetName
	 * @param row
	 * @param column
	 * @return String
	 */
	// public String getData(String sheetName, int row, int column){
	public String getData(int row, int column) {

		// XSSFCell cell = workSheet.getRow(row).getCell(column);
		// FormulaEvaluator evaluator =
		// workBook.getCreationHelper().createFormulaEvaluator();
		//
		// CellReference cellReference = new CellReference(cell);
		//
		// if(cell !=null){
		// switch(evaluator.evaluateFormulaCell(cell)){
		// case Cell.CELL_TYPE_BOOLEAN:
		//
		// }
		// }

		XSSFCell cellData = workSheet.getRow(row).getCell(column);
		String cellValue = "";

		// If we do not have a blank cell, we need to check. This check prevents
		// Null Pointer Exception
		if (cellData != null) {

			CellType type = cellData.getCellType();

			if (type == CellType.STRING) {
				cellValue = cellData.getStringCellValue();
			} else if (type == CellType.BOOLEAN) {
				cellValue = String.valueOf(cellData.getBooleanCellValue());

//			} else if (type == CellType.NUMERIC) {
//				cellValue = Double.toString(cellData.getNumericCellValue());
//			}

			} else if (type == CellType.NUMERIC) {

				// If the Cell Type is Numeric, convert it to a double.
				Double myDouble = cellData.getNumericCellValue();

				// Do do manipulation and omit 100.0 type numbers, conver it to
				// type BigDecimal
				BigDecimal myBigDecimal = new BigDecimal(myDouble);
				// Strip trailing zeros, e.g 1000.0 goes to 1000. 1000.50 stays
				// 1000.50
				myBigDecimal.stripTrailingZeros();
				// Create a formatter to return x.xx format
//				DecimalFormat decFormat = new DecimalFormat("#.##");
				DecimalFormat decFormat = new DecimalFormat("#.##########");

				cellValue = decFormat.format(myBigDecimal);
			}

			else if (type == CellType.BLANK) {
				cellValue = "";
			} else if (type == CellType.FORMULA) {
				cellValue = cellData.getRawValue();

			}

			else {
				try {
					throw new DatatypeConfigurationException(
							"The Cell Type was not found. Was not a String, boolean, or numeric.");
				} catch (DatatypeConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
		// Else if the cell is empty, return empty string. Having this here
		// prevents NullPointer where there are empty cells.
		else {
			cellValue = "";
		}

		return cellValue;

		// // workSheet = workBook.getSheet(sheetName);
		//
		// XSSFCell cellData = workSheet.getRow(row).getCell(column);
		//
		// CellType type = cellData.getCellTypeEnum();
		//
		// if (type == CellType.STRING) {
		// cellData.getStringCellValue();
		// } else if (type == CellType.BOOLEAN) {
		// cellData.getBooleanCellValue();
		// } else if (type == CellType.NUMERIC) {
		// cellData.getNumericCellValue();
		// } else if (type == CellType.BLANK){
		//// cellData = "";
		// }
		//
		// else {
		// try {
		// throw new DatatypeConfigurationException(
		// "The Cell Type was not found. Was not a String, boolean, or
		// numeric.");
		// } catch (DatatypeConfigurationException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// String myReturn = cellData.toString();
		//// myReturn = rowData.toString();
		//
		// return myReturn;
	}

	/**
	 * Returns a column index for the top row that matches the passed in column
	 * name.
	 * 
	 * @param columnName
	 * @return int
	 */
	public int getColumnIndex(String columnName) {

		int columnIndexForColumnName = -1;

		// Get the row where the column names are
		XSSFRow row = workSheet.getRow(0);

		// Variable to hold the last cell in the row.
		int lastCell = row.getLastCellNum();

		// Loop through until we find a column match
		for (int i = 0; i < lastCell; i++) {

			String currentCellValue = row.getCell(i).getStringCellValue().trim();

			if (columnName.equals(currentCellValue)) {
				columnIndexForColumnName = row.getCell(i).getColumnIndex();
				break;
			}
		}

		return columnIndexForColumnName;
	}

	/**
	 * This method returns the row index for a given column header and cell value.
	 * 
	 * @param columnHeader
	 * @param cellValue
	 * @return int - rowIndex
	 */
	public int getRowIndex(String columnHeader, String cellValue) {

		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Store the column index as it will be static each time
		int columnIndex = getColumnIndex(columnHeader);

		// Create an array list to hold all of the indexes for all records
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the passed in column
			// i+1 because we do NOT want the header row.
			String currentCellValue = getData((i + 1), columnIndex);

			// Add the currentCellValue to the array if it matches what we
			// passed in.
			if (currentCellValue.equals(cellValue)) {
				// Adds the row (Adds 1 to i because i is zero based)
				rowIndexArray.add(i + 1);
				break;
			}
		}

		// If we have a value in our array, we found match and we
		// should return it. If not, we should throw an exception to let the
		// user know.
		if (rowIndexArray.size() > 0) {
			// Assign the row index to be equal to the first position put into
			// the array.
			rowIndex = rowIndexArray.get(0);
		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet does not have a record with the value of "
							+ cellValue + " in it.");
		}
		return rowIndex;
	}

	/**
	 * This method returns the row index for a given row when two values are needed
	 * to make it unique.
	 * 
	 * @param columnHeader1
	 * @param cellValue1
	 * @param columnHeader2
	 * @param cellValue2
	 * @return int
	 */
	public int getRowIndexWithTwoValues(String columnHeader1, String cellValue1, String columnHeader2,
			String cellValue2) {

		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Store the column index as it will be static each time
		int columnIndex1 = getColumnIndex(columnHeader1);
		int columnIndex2 = getColumnIndex(columnHeader2);

		// Create an array list to hold all of the indexes for all records
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the passed in column
			// i+1 because we do NOT want the header row.
			String currentCellValue1 = getData((i + 1), columnIndex1);
			String currentCellValue2 = getData((i + 1), columnIndex2);

			// Add the currentCellValue to the array if it matches what we
			// passed in.
			if (currentCellValue1.equals(cellValue1) && currentCellValue2.equals(cellValue2)) {
				// Adds the row (Adds 1 to i because i is zero based)
				rowIndexArray.add(i + 1);
				break;
			}
		}

		// If we have a value in our array, we found match and we
		// should return it. If not, we should throw an exception to let the
		// user know.
		if (rowIndexArray.size() > 0) {
			// Assign the row index to be equal to the first position put into
			// the array.
			rowIndex = rowIndexArray.get(0);
		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet does not have a record with the value for "
							+ cellValue1 + " and " + cellValue2 + " in it.");
		}
		return rowIndex;
	}

	/**
	 * 
	 * NOTE: Uses "0" based index, so 15 actual rows would return 14.
	 * 
	 * @return
	 */
	public int getRowCount() {
		return workSheet.getLastRowNum();
	}

	/**
	 * Returns a random row in a datasheet.
	 * 
	 * @return int
	 */
	public int getRandomRow() {
		// Get the row count for the given sheet, and add 1 because of a zero
		// based index.

		// TODO - REMOVE ME
		System.out.println("***ROW COUNT*** " + getRowCount());
		int rowCount = getRowCount();

		// DO NOT get the first row, by passing "1" as the minimum number.
		int randomRow = AutomationHelper.generateRandomInteger(1, rowCount);

		// TODO - REMOVE ME
		System.out.println("***Random Row Index*** " + randomRow);

		return randomRow;
	}

	/**
	 * Returns a Random row index for a table with a "Data Used" column where the
	 * "Data Used" equals TRUE.
	 * 
	 * @return int
	 */
	public int getRandomRowIndex() {
		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Create an array list to hold all of the indexes for any FALSE
		// records.
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the unused (FALSE) rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String trueOrFalse = getData((i + 1), getColumnIndex("Data Used"));

			// If the data is TRUE, add it to the array for use.
			if (trueOrFalse.equalsIgnoreCase("TRUE")) {
				rowIndexArray.add(i + 1);// Adds the row (Adds 1 to i because it is zero based)
			}
		}

		// If we have a value in our array, we found a TRUE record and we should
		// return it. If not, we should throw an exception to let the user know.
		if (rowIndexArray.size() > 0) {
			// Pull a RANDOM value from the rowIndexArray.
			int arrayPosition = utilities.AutomationHelper.generateRandomInteger(1, rowIndexArray.size());

			rowIndex = rowIndexArray.get(arrayPosition - 1);

		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet has no more TRUE records in it.");
		}

		return rowIndex;
	}

	/**
	 * Returns a Random row index for a table within a column header and column
	 * value. For example, a Column name "Last Name" and a column value of "Smith".
	 * 
	 * @return int
	 */
	public int getRandomRowWithValue(String columnName, String columnValue) {
		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Create an array list to hold all of the indexes for any FALSE
		// records.
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the records in the datasheet where
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String currentCellValue = getData((i + 1), getColumnIndex(columnName));

			// If the data in the columnName matches the columnValue, add it to the list.
			if (currentCellValue.equalsIgnoreCase(columnValue)) {
				rowIndexArray.add(i + 1);// Adds the row (Adds 1 to i because it is zero based)
			}
		}

		// If we have a value in our array, we found a TRUE record and we should
		// return it. If not, we should throw an exception to let the user know.
		if (rowIndexArray.size() > 0) {
			// Pull a RANDOM value from the rowIndexArray.
			int arrayPosition = utilities.AutomationHelper.generateRandomInteger(1, rowIndexArray.size());

			rowIndex = rowIndexArray.get(arrayPosition - 1);

		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet has no records with the value "
							+ columnValue + " under the column name " + columnName + " in it.");
		}

		return rowIndex;
	}

	/**
	 * Reads the data sheet and checks for a user that has been registered AND has
	 * had a profile created. Picks a random row from the datasheet where this is
	 * true.
	 * 
	 * @return int
	 */
	public int getRandomRegisteredUserWithProfile() {

		// Obtain a row count of the datasheet. Necessary to get all records for
		// the FOR loop.
		int rowCount = getRowCount();
		// To store the random row index we will use for processing.
		int rowIndex;

		// Create an array list to hold all of the indexes for any TRUE
		// records.
		ArrayList<Integer> registeredUsersArray = new ArrayList<Integer>();
		// Create an array of items where users is true, but profile is false.
		ArrayList<Integer> profileCreatedArray = new ArrayList<Integer>();

		// Get all of the used (TRUE) rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String trueOrFalse = getData((i + 1), getColumnIndex("DataUsed"));

			// If the data is TRUE, add it to the array for use.
			if (trueOrFalse.equalsIgnoreCase("TRUE")) {
				registeredUsersArray.add(i + 1);// Adds the row (Adds 1 to i
												// because i
				// is zero based)
			}
		}

		// If we have a value in our registered users array, we found a TRUE
		// record and we should
		// return it. If not, we should throw an exception to let the user know.
		if (registeredUsersArray.size() > 0) {

			// Loop through all of the registered users to find a record where a
			// profile HAS BEEN created.
			for (int x : registeredUsersArray) {
				String trueOrFalse = getData(x, getColumnIndex("ProfileComplete"));

				if (trueOrFalse.equalsIgnoreCase("TRUE")) {
					profileCreatedArray.add(x);
				}
			}

			// If we do not have a in the profileCreatedArray, we should
			// throw an exception
			if (profileCreatedArray.isEmpty()) {
				throw new ArrayIndexOutOfBoundsException(
						"There are no records in the datasheet that have a registered user and also has a profile that HAS BEEN created.");
			}

		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray for registered users does not have a value in it, which means that the datasheet has no registered users records in it.");
		}

		// Get a random row for the datasheet where (1) The user is registered
		// and (2) the profile has been created.
		rowIndex = utilities.AutomationHelper.generateRandomInteger(1, profileCreatedArray.size());

		return rowIndex;

	}

	/**
	 * Returns the FIRST row index for a table with a "Data Used" column where the
	 * "Data Used" equals FALSE. This picks the first record that is not used.
	 * 
	 * @return int
	 */
	public int getNextUnusedDataRowIndex() {
		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Create an array list to hold all of the indexes for any FALSE
		// records.
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the unused (FALSE) rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String trueOrFalse = getData((i + 1), getColumnIndex("Data Used"));

			// If the data is FALSE, add it to the array for use.
			if (trueOrFalse.equalsIgnoreCase("FALSE")) {
				rowIndexArray.add(i + 1);// Adds the row (Adds 1 to i because i
											// is zero based)
			}
		}

		// If we have a value in our array, we found a FALSE record and we
		// should return it. If not, we should throw an exception to let the
		// user know.
		if (rowIndexArray.size() > 0) {
			// Assign the row index to be equal to the first position put into
			// the array.
			rowIndex = rowIndexArray.get(0);
		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet has no more FALSE records in it.");
		}
		return rowIndex;
	}

	public int getLastUsedRecordInTable() {
		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Create an array list to hold all of the indexes for any TRUE
		// records.
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the unused (FALSE) rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String trueOrFalse = getData((i + 1), getColumnIndex("Data Used"));

			// If the data is TRUE, add it to the array for use.
			if (trueOrFalse.equalsIgnoreCase("TRUE")) {
				rowIndexArray.add(i + 1);// Adds the row (Adds 1 to i because i
											// is zero based)
			}
		}

		// If we have a value in our array, we found a TRUE record and we
		// should return it. If not, we should throw an exception to let the
		// user know.
		if (rowIndexArray.size() > 0) {
			// Assign the row index to be equal to the size of the array, which
			// will be the last position of the TRUE value in the array
			rowIndex = rowIndexArray.size();
		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet has no more TRUE records in it.");
		}
		return rowIndex;
	}

	/**
	 * Returns the record count of a data sheet where the passed in column name has
	 * a value of TRUE in the row. It counts amount of records that are true.
	 * 
	 * @param columnName
	 * @return int
	 */
	public int getRecordCountWhereColumnIsTrue(String columnName) {
		// Row Index to return
		int rowIndex = -1;

		// Obtain a row count of the datasheet
		int rowCount = getRowCount();

		// Create an array list to hold all of the indexes for any TRUE
		// records.
		ArrayList<Integer> rowIndexArray = new ArrayList<Integer>();

		// Get all of the TRUE records for the passed in column name.
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String cellText = getData((i + 1), getColumnIndex(columnName));

			// If the data is TRUE, add it to the array for use.
			if (cellText.equalsIgnoreCase("TRUE")) {
				rowIndexArray.add(i + 1);// Adds the row (Adds 1 to i because i
											// is zero based)
			}
		}

		// If we have a value in our array, we found a TRUE record and we
		// should return it. If not, we should throw an exception to let the
		// user know.
		if (rowIndexArray.size() > 0) {
			// Assign the row index to be equal to the size of the array, which
			// will be the last position of the TRUE value in the array
			rowIndex = rowIndexArray.size();
		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray does not have a value in it, which means that the datasheet has no more TRUE records in it for the column "
							+ columnName);
		}
		return rowIndex;
	}

	/**
	 * Reads the data sheet and checks for a user that has been registered, but has
	 * not had a profile created.
	 * 
	 * @return int
	 */
	public int getRegisteredUserWithoutProfile() {

		// Obtain a row count of the datasheet. Necessary to get all records for
		// the FOR loop.
		int rowCount = getRowCount();

		// Create an array list to hold all of the indexes for any TRUE
		// records.
		ArrayList<Integer> registeredUsersArray = new ArrayList<Integer>();
		// Create an array of items where users is true, but profile is false.
		ArrayList<Integer> profileNotCreatedArray = new ArrayList<Integer>();

		// Get all of the unused (TRUE) rows in the datasheet
		for (int i = 0; i < rowCount; i++) {

			// Get the text from each row for the Data Used column
			// i+1 because we do NOT want the header row.
			String trueOrFalse = getData((i + 1), getColumnIndex("Data Used"));

			// If the data is TRUE, add it to the array for use.
			if (trueOrFalse.equalsIgnoreCase("TRUE")) {
				registeredUsersArray.add(i + 1);// Adds the row (Adds 1 to i
												// because i
				// is zero based)
			}
		}

		// If we have a value in our registered users array, we found a TRUE
		// record and we should
		// return it. If not, we should throw an exception to let the user know.
		if (registeredUsersArray.size() > 0) {

			// Loop through all of the registered users to find a record where a
			// profile has NOT been created.
			for (int x : registeredUsersArray) {
				String trueOrFalse = getData(x, getColumnIndex("Profile Complete"));

				if (trueOrFalse.equalsIgnoreCase("FALSE")) {
					profileNotCreatedArray.add(x);
				}
			}

			// If we do not have a in the profileNotCreatedArray, we should
			// throw an exception
			if (profileNotCreatedArray.isEmpty()) {
				throw new ArrayIndexOutOfBoundsException(
						"There are no records in the datasheet that have a registered user and also has a profile that has not been created.");
			}

		} else {
			throw new ArrayIndexOutOfBoundsException(
					"The rowIndexArray for registered users does not have a value in it, which means that the datasheet has no more TRUE records in it.");
		}

		// return the first row for a user that is registered but profile not
		// created
		return profileNotCreatedArray.get(0);

	}

	/**
	 * Sets a cell value in the row / column corresponding with the passed in data.
	 * 
	 * @param rowValue
	 * @param columnValue
	 * @param valueToSet
	 */
	public void writeToWorkSheet(String rowValue, String columnValue, String valueToSet) {

		writeToWorkSheet(getRowIndex(this.columnHeaderKey, rowValue), this.getColumnIndex(columnValue), valueToSet);
	}

	/**
	 * Sets a cell value in the row / column corresponding with the passed in data.
	 * 
	 * @param rowIndex
	 * @param columnValue
	 * @param valueToSet
	 */
	public void writeToWorkSheet(int rowIndex, String columnValue, String valueToSet) {

		writeToWorkSheet(rowIndex, this.getColumnIndex(columnValue), valueToSet);
	}

	/**
	 * Sets a cell value in the row / column corresponding with the passed in data.
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @param valueToSet
	 */
	public void writeToWorkSheet(int rowIndex, int columnIndex, String valueToSet) {
		// TODO: No idea how this method works. Work to understand at later
		// date.
		Reporter.log("Writing value '" + valueToSet + "' to worksheet '" + workSheet.toString() + "' in column index '"
				+ columnIndex + "'...", true);
		try {

			FileInputStream fsIP = new FileInputStream(filePath);

			XSSFWorkbook wb = new XSSFWorkbook(fsIP);
			XSSFSheet worksheet = wb.getSheet(workSheet.getSheetName());

			XSSFRow row = worksheet.getRow(rowIndex);

			// create a row if it does not exist
			if (row == null) {
				row = worksheet.createRow(rowIndex);
			}

			cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

			cell.setCellValue(valueToSet);

			fsIP.close();

			FileOutputStream output_file = new FileOutputStream(new File(filePath));

			wb.write(output_file);

			output_file.flush();
			output_file.close();
			wb.close();
			reset();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Reporter.log("Excel file updated", true);
	}

	/**
	 * Closes an open excel work book.
	 */
	public void closeFile() {
		try {
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

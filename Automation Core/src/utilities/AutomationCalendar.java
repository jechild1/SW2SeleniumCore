package utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import configuration.TestConfig;

/**
 * Project class to handle created and adjusting dates.
 * 
 * @author Jesse Childress
 *
 */
public class AutomationCalendar extends TestConfig{

	// Declare a global formatter to be used throughout the class.
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

	/**
	 * Method to return todays date
	 * 
	 * @return String
	 */
	public String getTodaysDate() {
		AutomationHelper.printMethodName();
		LocalDate date = LocalDate.now();
		return formatter.format(date);
	}

	/**
	 * Method to return todays date plus the days to increment.
	 * 
	 * @param daysToIncrement
	 * @return String
	 */
	public String getTodaysDatePlusDays(long daysToIncrement) {
		AutomationHelper.printMethodName();
		LocalDate todaysDate = LocalDate.parse(getTodaysDate(), formatter);
		todaysDate = todaysDate.plusDays(daysToIncrement);
		return formatter.format(todaysDate);
	}

	/**
	 * Method to return the date minus the days to decrement.
	 * 
	 * @param daysToDecrement
	 * @return String
	 */
	public String getTodaysDateMinusDays(long daysToDecrement) {
		AutomationHelper.printMethodName();

		// Ensure that we didn't pass in a negative number to decrement. When we call
		// minusDays, it expects a positive number.
		if (daysToDecrement < 1) {
			throw new RuntimeException("Please provide a positive number for days to decrement");
		}

		LocalDate todaysDate = LocalDate.parse(getTodaysDate(), formatter);
		todaysDate = todaysDate.minusDays(daysToDecrement);

		return formatter.format(todaysDate);
	}

	/**
	 * Method to accept a passed in date (format MM-dd-yyyy) and add days to it
	 * 
	 * @param date - MM-dd-yyyy format
	 * @param daysToIncrement - positive numbers only
	 * @return String - date
	 */
	public String getDatePlusDays(String date, long daysToIncrement) {
		AutomationHelper.printMethodName();

		// Convert the date to the correct object time
		LocalDate createdDate = LocalDate.parse(date, formatter);

		// Add the days to the passed in date
		createdDate = createdDate.plusDays(daysToIncrement);

		// return the results
		return formatter.format(createdDate);
	}

	/**
	 * Method to accept a passed in date (format MM-dd-yyyy) and subtract days from
	 * it
	 * 
	 * @param date - MM-dd-yyyy format
	 * @param daysToDecrement - positive numbers only
	 * @return String - date
	 */
	public String getDateMinusDays(String date, long daysToDecrement) {
		AutomationHelper.printMethodName();

		// Convert the date to the correct object time
		LocalDate createdDate = LocalDate.parse(date, formatter);

		// Subtract the days to the passed in date
		createdDate = createdDate.minusDays(daysToDecrement);

		// return the results
		return formatter.format(createdDate);
	}

}

package debuggingItems;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dates {

	public static void main(String[] args) {
		//Java 8 API
		LocalDate currentSystemDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = currentSystemDate.format(formatter);
		
		// TODO - Remove
//		System.out.println("Current Date: " + date);

//		MyCalorieIntakePageFactory calIntake = new MyCalorieIntakePageFactory(driver);
//		// create list of dates for last 7 days
//		// TODO - Create a method that only gets a few days . ex - 10
//		List<WebElement> entireCalorieTableDate = Tables.getTableRowsForPagingTable(driver,
//				calIntake.getMyCalorieIntakeTable());

		// for each date, see if it is in the table
		//Build an array of dates
		List<String> weekOfDates = new ArrayList<String>();
//		weekOfDates.add(date);
		for(int i=0; i<=7; i++){
			weekOfDates.add(currentSystemDate.minusDays(i).format(formatter));
			System.out.println(currentSystemDate.minusDays(i).format(formatter));
			System.out.println(currentSystemDate.minusDays(i).getDayOfWeek());
		}
		
		System.out.println();
		
		weekOfDates.forEach(System.out::println);
		
		//***********************************
		String totalDate = "11/30/2017";
		
		formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
		
		LocalDate localDate = LocalDate.parse(totalDate, formatter);
		
		int year = localDate.getYear() ;
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		
		System.out.println(year);
		System.out.println(month);
		System.out.println(day);
	}

}

package debuggingItems;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.testng.Assert;

public class Dates2 {

	public static void main(String[] args) {
		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
//		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd", Locale.ENGLISH);
//		
//		String totalDate = "11/30/2017";
//		
//		LocalDate localDate = LocalDate.parse(totalDate, formatter);
//		
//		System.out.println(localDate);
//		System.out.println(formatter2.format(localDate));
	
		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH);
//		String totalSleep = "11h 01m";
//		
//		// The following removes leading zeroes, but leaves one if necessary
//		// (i.e. it wouldn't just turn "0" to a blank string).
//		// The ^ anchor will make sure that the 0+ being matched is at the
//		// beginning of the input. The (?!$) negative lookahead ensures that not
//		// the entire string will be matched.
//		totalSleep = totalSleep.replace("h", "").replace("m", "")
//				.replace(" ", ":").replaceFirst("^0+(?!$)", "");
//		
//		LocalTime time = LocalTime.parse(totalSleep, formatter);
//		System.out.println(time);
//		System.out.println(totalSleep);
		
//		LocalTime time = null;
//		LocalTime appendedValue = LocalTime.of(1, 30);;
//		
//		appendedValue = appendedValue.plusHours(time.getHour()).plusMinutes(time.getMinute());
//		System.out.println(appendedValue);
		
		
//		LocalTime time1 = LocalTime.of(1, 30);
//		LocalTime time2 = LocalTime.of(1, 31);
//		
//		if(time1.equals(time2)) {
//			System.out.println("Success");
//			
//		}
//		
//		Assert.assertEquals(time1, time2);
		
		String totalDate = "6/5" + "/"	+ LocalDate.now().getYear();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		LocalDate date = LocalDate.parse(totalDate, formatter);
		
		System.out.println(totalDate);
		System.out.println(date);
		
		totalDate = date.format(formatter2);
		System.out.println(totalDate);

	}
}

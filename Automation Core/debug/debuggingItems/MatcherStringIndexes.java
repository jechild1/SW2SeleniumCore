package debuggingItems;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherStringIndexes {

	public static void main(String[] args) {
		
		String sentBy = "Bryson Mullins";
		String rowText = "Automation Test - Subject II Bryson Mullins 31 May 2018 07:30 Details";
		String dateLastSentDatasheet = "31 May 2018 07:30";
		
		//Variable to store the starting index of the sentBy field.
		int startIndex;
		//Variable to store the ending index of the sentBy field.
		int stopIndex = 0;
		
		Pattern pattern = Pattern.compile(sentBy);
		Matcher match = pattern.matcher(rowText);
		
		//While loop exists to keep running until we find the last match, if indeed multiples exist.
		while(match.find()) {
			 startIndex = match.start();
			 stopIndex = match.end();
		}
		
		
		String dateSentTFT = rowText.substring(stopIndex, rowText.indexOf("Details")).trim();
		
		System.out.println("String Date: " + dateSentTFT);
		
		// Formatter for processing a date into 08 May 2018 19:47 format.
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("dd MMM yyyy HH:mm", Locale.US);
		
		
		//LocalDateTime of row in TFT		
		LocalDateTime dateSentTFTLocalDateTime = LocalDateTime.parse(dateSentTFT, formatter);
		System.out.println("Local Date Time - TFT Application: " + dateSentTFTLocalDateTime);
		
		//LocalDateTime of datasheet
		LocalDateTime dateLastSentDatasheetLocalDateTime = LocalDateTime.parse(dateLastSentDatasheet, formatter);
		System.out.println("Local Date Time - Datasheet: " + dateLastSentDatasheetLocalDateTime);
		
		//Get the difference in minutes between the two times
		//Use Math.abs so that if the number is negative, it's adjusted to a positive number.
		long timeDifferenceInMin = Math.abs(ChronoUnit.MINUTES.between(dateSentTFTLocalDateTime, dateLastSentDatasheetLocalDateTime));

		if(timeDifferenceInMin <= 1) {
			System.out.println("The time differece between TFT and Datasheet is: " + timeDifferenceInMin + " minutes.");
		}else {
			
		}
		
		
		
		
		

	}

}

package debuggingItems;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LocalDates {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		
		List<String> currentWeekDates = new ArrayList<String>();
		for (int i = 1; i <= 7; i++) {
			currentWeekDates.add(formatter.format(LocalDate.now().minusDays(i)).toString());
			
		}
		
		//Print array
		for(String x: currentWeekDates) {
			System.out.println(x);
		}

	}

}

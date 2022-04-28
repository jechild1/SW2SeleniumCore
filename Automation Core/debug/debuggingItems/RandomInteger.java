package debuggingItems;

import java.util.concurrent.ThreadLocalRandom;

public class RandomInteger {

	public static void main(String[] args) {

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = ThreadLocalRandom.current().nextInt(1, 8+1);
		
		System.out.println(randomNum);
	}

}

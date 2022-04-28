package debuggingItems;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Robots {

	public static void main(String[] args) {
		
		
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}

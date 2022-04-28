package configuration;

import org.uncommons.reportng.HTMLReporter;

/**
 * Some default overrides for HTMLReporter that can be used by all projects
 * 
 * @author scott.brazelton
 * 
 * extends HTMLReporter
 *
 */
public class EnhancedHTMLReporter extends HTMLReporter {
	private static final String FRAMES_PROPERTY = "org.uncommons.reportng.frames";
	private static final String CUSTOM_STYLESHEET = "org.uncommons.reportng.stylesheet";

	public EnhancedHTMLReporter() {
		super();
		
		//set frames property if not already set
		String frames = System.getProperty(FRAMES_PROPERTY);
		if (frames == null) {
			System.setProperty(FRAMES_PROPERTY, "true");
		}
			
		//set template if not already set
		String cssPath = System.getProperty(CUSTOM_STYLESHEET);
		if (cssPath == null) {
			System.setProperty(CUSTOM_STYLESHEET, "..\\selenium-core\\templates\\reportng.css");
		}
	}
	
}

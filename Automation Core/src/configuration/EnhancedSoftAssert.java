package configuration;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

/**
 * Class to extend "SoftAssert" for enhanced logging.
 * 
 * @author scott.brazelton
 *
 */
public class EnhancedSoftAssert extends SoftAssert {

	/**
	 * Enhanced onAssertSuccess to allow reporter logging
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onAssertSuccess(IAssert<?> assertCommand) {

		String validationName = assertCommand.getMessage();

		Reporter.log(String.format("<b style='color:green;'>Validation:</b> %s",
				validationName == null ? "Unknown" : validationName));
		Reporter.log("<br>");
		Reporter.log(String.format("<b style='color:green;'>Matches:</b> %s",
				assertCommand.getActual()));
		Reporter.log("<br>");

		super.onAssertSuccess(assertCommand);
	}

//	/**
//	 * Enhanced onAssertFailure to allow reporter logging
//	 * 
//	 * {@inheritDoc}
//	 * 
//	 */
//	@Override
//	public void onAssertFailure(IAssert<?> assertCommand) {
//
//		super.onAssertFailure(assertCommand);
//	}

	/**
	 * Enhanced onAssertFailure to allow reporter logging
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {

		validationErrorLogMessage(assertCommand);

		super.onAssertFailure(assertCommand, ex);
	}

	/**
	 * Generates log message for when a soft assert fails
	 * 
	 * @param assertCommand
	 */
	private void validationErrorLogMessage(IAssert<?> assertCommand) {

		String validationName = assertCommand.getMessage();

		Reporter.log(String.format("<b style='color:red;'>Validation:</b> %s",
				validationName == null ? "Unknown" : validationName));
		Reporter.log("<br>");
		Reporter.log(String.format("<b style='color:red;'>Expected:</b> %s",
				assertCommand.getExpected()));
		Reporter.log("<br>");
		Reporter.log(String.format("<b style='color:red;'>Actual:</b> %s",
				assertCommand.getActual()));
		Reporter.log("<br>");

		Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
	}
}

package fi.arcusys.koku.palvelut.util;

import java.util.regex.Pattern;

/**
 * Simple validator class which checks that XML-message doesn't
 * contain any get* or find* methods.
 * 
 * @author Toni Turunen
 *
 */
public class OperationsValidatorImpl implements OperationsValidator {

	private static final String ILLEGAL_OPERATIONS_REGEX = "<soa:get|<soa:find";
	/* FIXME: Pattern is immutable class, but it is safe to call matcher method? */
	private static final Pattern pattern = Pattern.compile(ILLEGAL_OPERATIONS_REGEX);
	
	@Override
	public boolean isValid(String xmlData) {
		if (pattern.matcher(xmlData).find()) {
			return false;
		} else {
			return true;
		}
	}
}

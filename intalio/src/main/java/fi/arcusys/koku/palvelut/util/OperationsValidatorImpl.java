package fi.arcusys.koku.palvelut.util;

import java.util.regex.Pattern;

public class OperationsValidatorImpl implements OperationsValidator {

	private static final String ILLEGAL_OPERATIONS_REGEX = "<soa:get|<soa:find";
	private final Pattern pattern = Pattern.compile(ILLEGAL_OPERATIONS_REGEX);
	
	@Override
	public boolean isValid(String xmlData) {
		if (pattern.matcher(xmlData).find()) {
			return false;
		} else {
			return true;
		}
	}

}

package cz.martinbayer.logparser.log4j2.pattern.monitors.types;

import cz.martinbayer.logparser.log4j2.monitors.pattern.PatternParser;
import cz.martinbayer.logparser.log4j2.pattern.monitors.model.TypePattern;

/**
 * Compounded end of the log is used, there is either message followed by
 * "Error No:" or only the message
 * 
 * @author Martin
 * 
 */
public class MessageExceptionPattern implements TypePattern {

	public static final String GROUP_NAME_MESSAGE = "message";
	public static final String GROUP_NAME_EXCEPTION = "exception";

	private static final String MESSAGE_EXCEPTION_REGEX = "?<"
			+ GROUP_NAME_MESSAGE + ">.*?(?=Error No:)(?<"
			+ GROUP_NAME_EXCEPTION + ">Error No:.*+)|.*+";

	@Override
	public String[] getGroupNames() {
		return new String[] { GROUP_NAME_MESSAGE, GROUP_NAME_EXCEPTION };
	}

	@Override
	public String getRegex() {
		StringBuffer sb = new StringBuffer();
		sb.append(PatternParser.GROUP_START).append(MESSAGE_EXCEPTION_REGEX)
				.append(PatternParser.GROUP_END);
		return sb.toString();
	}
}

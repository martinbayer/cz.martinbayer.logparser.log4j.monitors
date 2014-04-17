package cz.martinbayer.logparser.log4j2.pattern.monitors.types;

import cz.martinbayer.logparser.log4j2.monitors.pattern.PatternParser;
import cz.martinbayer.logparser.log4j2.pattern.monitors.model.TypePattern;

public class ThreadPattern implements TypePattern {

	public static final String GROUP_NAME = "thread";

	private static final String MESSAGE_REGEX = "?<"
			+ GROUP_NAME
			+ ">\\S+(?=\\s\\d{2}\\/\\d{2}\\/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{4})";

	@Override
	public String[] getGroupNames() {
		return new String[] { GROUP_NAME };
	}

	@Override
	public String getRegex() {
		StringBuffer sb = new StringBuffer();
		sb.append(PatternParser.GROUP_START).append(MESSAGE_REGEX)
				.append(PatternParser.GROUP_END);
		return sb.toString();
	}
}

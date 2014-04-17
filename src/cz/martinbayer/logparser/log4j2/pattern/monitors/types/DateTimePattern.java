package cz.martinbayer.logparser.log4j2.pattern.monitors.types;

import cz.martinbayer.logparser.log4j2.monitors.pattern.PatternParser;
import cz.martinbayer.logparser.log4j2.pattern.monitors.model.TypePattern;

public class DateTimePattern implements TypePattern {

	public static final String GROUP_NAME = "datetime";
	private static String MONITOR_DATE_TIME_REGEX = "?<" + GROUP_NAME
			+ ">\\d{2}\\/\\d{2}\\/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{4}";

	@Override
	public String getRegex() {
		StringBuffer sb = new StringBuffer();
		sb.append(PatternParser.GROUP_START).append(MONITOR_DATE_TIME_REGEX)
				.append(PatternParser.GROUP_END);
		return sb.toString();
	}

	@Override
	public String[] getGroupNames() {
		return new String[] { GROUP_NAME };
	}
}

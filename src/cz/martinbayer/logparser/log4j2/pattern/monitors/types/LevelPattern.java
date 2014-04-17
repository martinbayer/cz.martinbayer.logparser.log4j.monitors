package cz.martinbayer.logparser.log4j2.pattern.monitors.types;

import cz.martinbayer.logparser.log4j2.monitors.pattern.PatternParser;
import cz.martinbayer.logparser.log4j2.pattern.monitors.model.TypePattern;

public class LevelPattern implements TypePattern {

	public static final String GROUP_NAME = "level";

	private static final String LEVELS_REGEX = "?<" + GROUP_NAME
			+ ">ERROR|WARN|DEBUG|INFO";

	@Override
	public String[] getGroupNames() {
		return new String[] { GROUP_NAME };
	}

	@Override
	public String getRegex() {
		StringBuffer sb = new StringBuffer();
		sb.append(PatternParser.GROUP_START).append(LEVELS_REGEX)
				.append(PatternParser.GROUP_END);
		return sb.toString();
	}
}

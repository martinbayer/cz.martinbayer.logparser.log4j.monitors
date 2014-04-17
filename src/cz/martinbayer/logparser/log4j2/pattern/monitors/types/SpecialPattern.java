package cz.martinbayer.logparser.log4j2.pattern.monitors.types;

import cz.martinbayer.logparser.log4j2.monitors.pattern.PatternParser;
import cz.martinbayer.logparser.log4j2.pattern.monitors.model.TypePattern;

/**
 * intended to be used for spaces, brackets and other characters which are not
 * part of groups
 */
public class SpecialPattern implements TypePattern {

	private String pattern;

	public SpecialPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String[] getGroupNames() {
		return null;
	}

	@Override
	public String getRegex() {
		return pattern;
	}
}

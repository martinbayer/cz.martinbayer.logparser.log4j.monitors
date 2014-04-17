package cz.martinbayer.logparser.log4j2.monitors.pattern;

import java.util.ArrayList;

public class ConfigPatternInfo {

	private ArrayList<String> usedGroups = new ArrayList<>();

	public void addGroups(String[] strings) {
		for (int i = 0; strings != null && i < strings.length; i++) {
			if (strings[i] != null) {
				usedGroups.add(strings[i]);
			}
		}
	}

	public ArrayList<String> getGroups() {
		return usedGroups;
	}
}

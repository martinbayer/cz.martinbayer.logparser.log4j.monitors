package cz.martinbayer.logparser.log4j2.monitors.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.martinbayer.logparser.fileshandler.LogFileReceiver;
import cz.martinbayer.logparser.fileshandler.LogFileSemaphoreWatchedStore;
import cz.martinbayer.logparser.log4j2.monitors.pattern.ConfigPatternInfo;
import cz.martinbayer.logparser.log4j2.pattern.monitors.model.TypePattern;
import cz.martinbayer.logparser.log4j2.pattern.monitors.types.DateTimePattern;
import cz.martinbayer.logparser.log4j2.pattern.monitors.types.LevelPattern;
import cz.martinbayer.logparser.log4j2.pattern.monitors.types.MessageExceptionPattern;
import cz.martinbayer.logparser.log4j2.pattern.monitors.types.SpecialPattern;
import cz.martinbayer.logparser.log4j2.pattern.monitors.types.ThreadPattern;
import cz.martinbayer.logparser.logic.ILogParserEvent;
import cz.martinbayer.logparser.logic.ILogParserListener;
import cz.martinbayer.logparser.logic.LogParserPhase;

public class Log4jMonitorReader extends LogFileReceiver {

	private ILogParserListener listener;
	private Pattern parsedPattern;
	private ConfigPatternInfo info;
	private Pattern splitPattern;

	public Log4jMonitorReader(LogFileSemaphoreWatchedStore semaphore) {
		super(semaphore);
		info = new ConfigPatternInfo();
		initPattern();
	}

	private void initPattern() {
		TypePattern[] patterns = new TypePattern[] { new ThreadPattern(),
				new SpecialPattern("\\s"), new DateTimePattern(),
				new SpecialPattern("\\s"), new LevelPattern(),
				new SpecialPattern("\\s?:\\s"), new MessageExceptionPattern() };
		StringBuffer mainPattern = new StringBuffer();
		for (TypePattern pattern : patterns) {
			info.addGroups(pattern.getGroupNames());
			mainPattern.append(pattern.getRegex());
		}
		/* split the record by thread name and date time */
		splitPattern = Pattern.compile("(?s)" + patterns[0].getRegex());
		parsedPattern = Pattern.compile("(?s)" + mainPattern);
	}

	/**
	 * Listener must be set before the processing is started otherwise exception
	 * is thrown
	 * 
	 * @param listener
	 */
	public void setListener(ILogParserListener listener) {
		this.listener = listener;
	}

	@Override
	public int handleStoredBuffer(StringBuffer sb, int actualLength) {
		int lastFound = -1;
		if (listener == null) {
			throw new UnsupportedOperationException(
					"Operation is not allows if no listener is registered");
		}
		Matcher localMatcher = splitPattern.matcher(sb.substring(0,
				actualLength));

		int firstIdx = -1, lastIdx = -1;
		Matcher wholePartMatcher;

		/* get initial position of the pattern */
		if (localMatcher.find()) {
			/* and its location if it was found */
			firstIdx = localMatcher.start();
		}
		while (firstIdx >= 0) {
			if (localMatcher.find()) {
				lastIdx = localMatcher.start();
				wholePartMatcher = parsedPattern.matcher(sb.substring(firstIdx,
						lastIdx));
			} else {
				lastIdx = -1;
				wholePartMatcher = parsedPattern.matcher(sb.substring(firstIdx,
						actualLength));
			}
			// System.out.println("[" + firstIdx + "," + lastIdx + "]");

			if (wholePartMatcher.find()) {
				this.listener.parsed(new ILogParserEvent(this,
						LogParserPhase.START, null, null));
				String value;
				for (String group : info.getGroups()) {
					/*
					 * invoke listener only if there is some value for the group
					 */
					if ((value = wholePartMatcher.group(group)) != null) {
						this.listener.parsed(new ILogParserEvent(this,
								LogParserPhase.PROPERTY, group, value));
					}
				}
				this.listener.parsed(new ILogParserEvent(this,
						LogParserPhase.FINISH, null, null));
			}

			lastFound = lastIdx < 0 ? actualLength : lastIdx;
			firstIdx = lastIdx;
		}
		return lastFound;
	}

	@Override
	protected void releaseSources() {
		listener = null;

	}
}

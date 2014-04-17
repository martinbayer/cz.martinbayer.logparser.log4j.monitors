package cz.martinbayer.logparser.log4j2.monitors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.martinbayer.logparser.log4j2.monitors.handler.Log4JMonitorHandler;
import cz.martinbayer.logparser.logic.ILogParserEvent;
import cz.martinbayer.logparser.logic.ILogParserListener;
import cz.martinbayer.logparser.logic.LogParserPhase;

public class Main {

	public static void main(String[] args) throws IOException {
		String log4jJavaRegex = "(?s)(^\\S++)\\s(\\d{2}\\/\\d{2}\\/\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{4})\\s(ERROR|WARN|DEBUG|INFO)\\s?:\\s(.*?(?=Error No:)(Error No:.*+)|.*+)";
		// plain regex:
		// (?s)(\S++)\s(\d{2}\/\d{2}\/\d{4}\s\d{2}:\d{2}:\d{2}\.\d{4})\s(ERROR|WARN|DEBUG|INFO)\s?:\s(.*?(?=Error
		// No:)(Error No:.*+)|.*+)";
		// (?s)(?<thread>^\S++)\s(?<datetime>\d{2}\/\d{2}\/\d{4}\s\d{2}:\d{2}:\d{2}\.\d{4})\s(?<level>ERROR|WARN|DEBUG|INFO)\s?:\s(<message>.*?(?=Error
		// No:)(?<exception>Error No:.*+)|.*+)";
		// (?s)(?<thread>^\S++)\s(?<datetime>\d{2}\/\d{2}\/\d{4}\s\d{2}:\d{2}:\d{2}\.\d{4})\s(?<level>ERROR|WARN|DEBUG|INFO)\s?:\s(<message>.*?(?=Error
		// No:)(?<exception>Error No:.*+)|.*+)
		String encoding = "UTF-8";
		String filePath = "D:\\School\\Mgr\\Diploma thesis\\log4j";
		Log4JMonitorHandler handler = Log4JMonitorHandler.getInstance(new File(
				filePath).listFiles(), encoding);

		// Log4JMonitorHandler handler = Log4JMonitorHandler
		// .getInstance(
		// new File[] { new File(
		// "D:\\School\\Mgr\\Diploma thesis\\log4j\\imfplus_EMS.log.2") },
		// encoding);

		// LogBackHandler handler = LogBackHandler
		// .getInstance(
		// new File[] { new File(
		// "D:\\School\\Mgr\\Diploma thesis\\logback\\logs\\client_debug.log")
		// },
		// pattern, encoding);
		final FileOutputStream fs = new FileOutputStream(new File(
				"c:\\ahoj.txt"));
		FunParserListener listener = new FunParserListener();
		System.out.println(new SimpleDateFormat("yyyy MMM dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
		handler.doParse(listener);
		System.out.println(new SimpleDateFormat("yyyy MMM dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
		fs.flush();
		fs.close();

		System.out.println("count:" + listener.getObjectsCount());

	}
}

class FunParserListener implements ILogParserListener {
	ArrayList<FunObject> objects = new ArrayList<>();
	FunObject object;

	@Override
	public void parsed(ILogParserEvent event) {
		if (event.getPhase() == LogParserPhase.START) {
			object = new FunObject();
		} else if (event.getPhase() == LogParserPhase.FINISH) {
			objects.add(object);
			object = null;
		} else {
			if (object == null || event.getGroupName() == null) {
				throw new NullPointerException("Log event haven't started yet");
			}
			// if (event.getGroupName().equals("datetime")) {
			// System.out.println(event.getGroupValue());
			// }
		}
	}

	public long getObjectsCount() {
		return objects.size();
	}
}

class FunObject {
	private String error;
	String time, message, line, file, level, thread;
	static int count = 0;

	public void setError(String error) {
		count++;
		System.out
				.println("------------------------------------------------------------");
		System.out.println(error);
		System.out
				.println("----------------------------------------------------------"
						+ count);
	}
}
package cz.martinbayer.logparser.log4j2.monitors.handler;

import java.io.File;

import cz.martinbayer.logparser.fileshandler.LogFileReader;
import cz.martinbayer.logparser.fileshandler.LogFileSemaphoreWatchedStore;
import cz.martinbayer.logparser.logic.ILogParserListener;

/**
 * 
 * @author Martin
 * 
 */
public class Log4JMonitorHandler {

	private static Log4JMonitorHandler instance;
	private File[] filesToParse;
	private String encoding = "UTF-8";
	private LogFileReader logFileReader;
	private Log4jMonitorReader receiver;
	private LogFileSemaphoreWatchedStore semaphore;

	private Log4JMonitorHandler(File[] filesToParse, String encoding) {
		this.filesToParse = filesToParse;
		if (encoding != null) {
			this.encoding = encoding;
		}
		semaphore = new LogFileSemaphoreWatchedStore(5);
		logFileReader = new LogFileReader(semaphore, this.filesToParse,
				this.encoding);
		receiver = new Log4jMonitorReader(semaphore);
	}

	public static synchronized Log4JMonitorHandler getInstance(
			File[] filesToParse, String encoding) {
		if (instance == null) {
			instance = new Log4JMonitorHandler(filesToParse, encoding);
		}
		return instance;
	}

	/**
	 * Only one listener can be used at once due to performance reason. Result
	 * can be used for other listeners in upper layer if needed.
	 * 
	 * @param listener
	 */
	public synchronized void doParse(ILogParserListener listener) {
		semaphore.reset();
		receiver.setListener(listener);
		Thread fileReadThread = new Thread(logFileReader);
		Thread logbackReceiverThread = new Thread(receiver);
		fileReadThread.start();
		logbackReceiverThread.start();

		try {
			logbackReceiverThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

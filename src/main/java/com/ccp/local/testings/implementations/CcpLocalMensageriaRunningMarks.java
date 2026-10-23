package com.ccp.local.testings.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ccp.decorators.CcpFileDecorator;
import com.ccp.decorators.CcpFolderDecorator;
import com.ccp.decorators.CcpStringDecorator;

/**
 * The messages of the local asynchronous messaging still running, shared by the local processes: the REST APIs run
 * with {@code AsyncMensageriaListener} and answer before their writes end, and a test in another process waits for
 * them. Each message leaves a mark, one file named by the time it was published, in a folder every local process sees;
 * the mark is left before the publisher goes on (so a task started by another one is marked before the first one
 * ends) and removed when the task ends, with or without success.
 */
public final class CcpLocalMensageriaRunningMarks {

	/** The folder of the marks. */
	private static final String RUNNING_FOLDER = "c:/logs/mensageria/running/";

	/** Separates the publishing time from the unique part in the name of a mark. */
	private static final String NAME_SEPARATOR = "-";

	/** Utility class; not instantiable. */
	private CcpLocalMensageriaRunningMarks() {}

	/**
	 * Leaves the mark of a message being published now.
	 * @return the name of the mark
	 */
	static String mark() {
		long now = System.currentTimeMillis();
		UUID uniquePart = UUID.randomUUID();
		String markName = now + NAME_SEPARATOR + uniquePart;
		CcpFileDecorator runningMark = getRunningMark(markName);
		runningMark.write(markName);
		return markName;
	}

	/**
	 * Removes the mark of a message whose task ended.
	 * @param markName the name of the mark
	 */
	static void unmark(String markName) {
		CcpFileDecorator runningMark = getRunningMark(markName);
		runningMark.remove();
	}

	/**
	 * Tells whether some message published after the given time is still running, in any local process.
	 * @param time the time, in milliseconds
	 * @return {@code true} when such a message has not ended yet
	 */
	public static boolean isAnyRunningPublishedAfter(long time) {
		CcpStringDecorator runningFolderPath = new CcpStringDecorator(RUNNING_FOLDER);
		CcpFolderDecorator runningFolder = runningFolderPath.folder();
		List<String> markNames = new ArrayList<>();
		runningFolder.readFiles(runningMark -> markNames.add(runningMark.getName()));
		for (String markName : markNames) {
			int separatorPosition = markName.indexOf(NAME_SEPARATOR);
			String publishingTimeText = markName.substring(0, separatorPosition);
			long publishingTime = Long.parseLong(publishingTimeText);
			boolean publishedAfter = publishingTime > time;
			if(publishedAfter) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The file of a mark.
	 * @param markName the name of the mark
	 * @return the file
	 */
	private static CcpFileDecorator getRunningMark(String markName) {
		String runningMarkPath = RUNNING_FOLDER + markName;
		CcpStringDecorator runningMarkPathDecorator = new CcpStringDecorator(runningMarkPath);
		CcpFileDecorator runningMark = runningMarkPathDecorator.file();
		return runningMark;
	}
}

package com.ccp.local.testings.implementations;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.mensageria.receiver.CcpMensageriaReceiver;

/**
 * Run, in its own thread, of a message of {@code AsyncMensageriaListener}: the process of the topic through the receiver
 * named in the message, as a real consumer does. When the process ends, with or without success, the running mark of
 * the message ({@link CcpLocalMensageriaRunningMarks}) is removed; a failure goes on to the standard error of the
 * process.
 */
class AsyncMensageriaProcessRun implements Runnable {

	/** The topic (the class name of the process). */
	private final String topic;

	/** The message. */
	private final CcpJsonRepresentation json;

	/** The name of the running mark of the message. */
	private final String runningMarkName;

	/**
	 * Keeps the message to run.
	 * @param topic the topic (the class name of the process)
	 * @param json the message
	 * @param runningMarkName the name of the running mark of the message
	 */
	AsyncMensageriaProcessRun(String topic, CcpJsonRepresentation json, String runningMarkName) {
		this.topic = topic;
		this.json = json;
		this.runningMarkName = runningMarkName;
	}

	/** Runs the process of the topic and removes the running mark of the message. */
	public void run() {
		try {
			CcpMensageriaReceiver receiver = CcpMensageriaReceiver.getInstance(this.json);
			receiver.executeProcess(this.topic, this.json);
		} finally {
			CcpLocalMensageriaRunningMarks.unmark(this.runningMarkName);
		}
	}
}

package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

/**
 * {@code CcpMensageriaSender} mock for local tests. Instead of sending messages to
 * Pub/Sub, it runs the topic's process directly, each one in its own thread, via {@code CcpMensageriaReceiver}.
 */
class AsyncMensageriaListener implements CcpMensageriaSender {
	/** The single instance: the class keeps no state. */
	public final static AsyncMensageriaListener INSTANCE = new AsyncMensageriaListener();

	/** Builds the listener. */
	private AsyncMensageriaListener() {}

	/**
	 * Runs, for each message, the process of the topic in a new thread, through the receiver named in the message.
	 * Failures are not reported to the caller: they show up in the standard error of the process. Each message is marked
	 * as running ({@link CcpLocalMensageriaRunningMarks}) here, before its thread starts, so whoever waits for the writes
	 * of a request sees it before the request is answered.
	 * @param topic the topic (the class name of the process)
	 * @param msgs the serialized messages
	 * @return this sender
	 */
	public CcpMensageriaSender sendToMensageria(String topic, String... msgs) {

		for (String msg : msgs) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(msg);
			String runningMarkName = CcpLocalMensageriaRunningMarks.mark();
			AsyncMensageriaProcessRun processRun = new AsyncMensageriaProcessRun(topic, json, runningMarkName);
			Thread processThread = new Thread(processRun);
			processThread.start();

		}

		return this;
	}

}

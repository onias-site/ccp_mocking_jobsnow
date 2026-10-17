package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.mensageria.receiver.CcpMensageriaReceiver;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

/**
 * {@code CcpMensageriaSender} mock for local tests. Instead of sending messages to
 * Pub/Sub, it runs the topic's process directly, each one in its own thread, via {@code CcpMensageriaReceiver}.
 */
class AsyncMensageriaListener implements CcpMensageriaSender {

	/** Builds the listener. */
	public AsyncMensageriaListener() {}

	/**
	 * Runs, for each message, the process of the topic in a new thread, through the receiver named in the message, which
	 * records the outcome (success or failure) as a real consumer does. Failures are not reported to the caller; until
	 * 2026-10-15 they were not recorded either, and showed up only in the standard error of the process.
	 * @param topic the topic (the class name of the process)
	 * @param msgs the serialized messages
	 * @return this sender
	 */
	public CcpMensageriaSender sendToMensageria(String topic, String... msgs) {

		for (String msg : msgs) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(msg);
			new Thread(() -> {
				CcpMensageriaReceiver receiver = CcpMensageriaReceiver.getInstance(json);
				receiver.executeProcessAndRecordOutcome(topic, json);
			}).start();

		}

		return this;
	}

}

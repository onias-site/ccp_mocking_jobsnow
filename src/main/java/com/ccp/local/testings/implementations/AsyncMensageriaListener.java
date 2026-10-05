package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.business.CcpBusiness;
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
	 * Runs, for each message, the process of the topic in a new thread, through the receiver named in the message.
	 * Failures happen in the threads and are not reported to the caller.
	 * @param topic the topic (the class name of the process)
	 * @param msgs the serialized messages
	 * @return this sender
	 */
	public CcpMensageriaSender sendToMensageria(String topic, String... msgs) {

		for (String msg : msgs) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(msg);
			new Thread(() -> {
				CcpMensageriaReceiver receiver = CcpMensageriaReceiver.getInstance(json);
				CcpBusiness process = receiver.getProcess(topic, json);
				process.execute(json);
			}).start();

		}

		return this;
	}

}

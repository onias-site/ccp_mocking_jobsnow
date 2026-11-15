package com.ccp.local.testings.implementations;

import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

/**
 * {@code CcpMensageriaSender} of the local REST APIs that emulates a Pub/Sub push subscription: each message is
 * delivered, in a thread of its own, as an HTTP {@code POST} to the local push consumer
 * ({@code jn_mensageria-consumer_gcp-pubsub-push-spring_dependency}, port 8083), in the envelope Pub/Sub uses. The API
 * answers before the consumer ends, as in production, and does not need the libraries the consumer uses (e-mail,
 * instant messenger, bulk, query). The running mark of the message ({@link CcpLocalMensageriaRunningMarks}) is left
 * before the publisher goes on and removed when the consumer answers, so a test in another process can wait for it.
 */
class PubSubPushEmulator implements CcpMensageriaSender {
	/** The single instance: the class keeps no state. */
	public final static PubSubPushEmulator INSTANCE = new PubSubPushEmulator();

	/** Use {@link #INSTANCE}. */
	private PubSubPushEmulator() {}

	/**
	 * Marks each message as running and delivers it to the push consumer in a thread of its own.
	 * @param topic the topic (the class name of the process, with its qualifiers)
	 * @param msgs the messages
	 * @return this sender
	 */
	public CcpMensageriaSender sendToMensageria(String topic, String... msgs) {

		for (String msg : msgs) {
			String runningMarkName = CcpLocalMensageriaRunningMarks.mark();
			PubSubPushEmulatorDelivery delivery = new PubSubPushEmulatorDelivery(topic, msg, runningMarkName);
			Thread deliveryThread = new Thread(delivery);
			deliveryThread.start();
		}

		return this;
	}

}

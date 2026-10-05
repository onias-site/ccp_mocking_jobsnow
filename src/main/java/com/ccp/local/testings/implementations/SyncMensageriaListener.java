package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.business.CcpBusiness;
import com.ccp.especifications.mensageria.receiver.CcpMensageriaReceiver;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

/**
 * {@code CcpMensageriaSender} mock for local tests. Instead of sending messages to
 * Pub/Sub, it runs the topic's process directly and synchronously via {@code CcpMensageriaReceiver}.
 */
class SyncMensageriaListener implements CcpMensageriaSender {

	/** Builds the listener. */
	public SyncMensageriaListener() {}

	/**
	 * Runs, for each message and in the calling thread, the process of the topic through the receiver named in the message.
	 * @param topic the topic (the class name of the process)
	 * @param msgs the serialized messages
	 * @return this sender
	 */
	public CcpMensageriaSender sendToMensageria(String topic, String... msgs) {

		for (String msg : msgs) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(msg);
//			new Thread(() -> {
//				CcpBusiness process = CcpAsyncTask.getProcess(topic);
//				process.apply(messageDetails); 
//			}).start();

			CcpMensageriaReceiver receiver = CcpMensageriaReceiver.getInstance(json);
			CcpBusiness process = receiver.getProcess(topic, json);
			process.execute(json);
		}

		return this;
	}

}

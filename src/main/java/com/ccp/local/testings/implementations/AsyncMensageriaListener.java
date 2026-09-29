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

	public AsyncMensageriaListener() {}

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

package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.mensageria.receiver.CcpMensageriaReceiver;
import com.ccp.especifications.mensageria.receiver.CcpTopic;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

class LocalMensageriaSender implements CcpMensageriaSender {

	public LocalMensageriaSender() {}

	public CcpMensageriaSender send(String topic, String... msgs) {

		for (String msg : msgs) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(msg);
//			new Thread(() -> {
//				Function<CcpJsonRepresentation, CcpJsonRepresentation> process = CcpAsyncTask.getProcess(topic);
//				process.apply(messageDetails); 
//			}).start();

			CcpMensageriaReceiver receiver = CcpMensageriaReceiver.getInstance(json);
			CcpTopic process = receiver.getProcess(topic, json);
			process.apply(json);
			
		}
		return this;
	}

}

package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.mensageria.receiver.CcpBusiness;
import com.ccp.especifications.mensageria.receiver.CcpMensageriaReceiver;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

class LocalMensageriaSender implements CcpMensageriaSender {

	public LocalMensageriaSender() {}

	public CcpMensageriaSender sendToMensageria(String topic, String... msgs) {

		for (String msg : msgs) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(msg);
//			new Thread(() -> {
//				CcpBusiness process = CcpAsyncTask.getProcess(topic);
//				process.apply(messageDetails); 
//			}).start();

			CcpMensageriaReceiver receiver = CcpMensageriaReceiver.getInstance(json);
			CcpBusiness process = receiver.getProcess(topic, json);
			process.apply(json);
		}

		return this;
	}

}

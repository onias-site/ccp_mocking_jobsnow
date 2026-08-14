package com.ccp.local.testings.implementations;


import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.business.CcpBusiness;
import com.ccp.especifications.mensageria.receiver.CcpMensageriaReceiver;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;

/**
 * Mock de {@code CcpMensageriaSender} para testes locais. Em vez de enviar mensagens para o
 * Pub/Sub, executa o processo do tópico diretamente de forma síncrona via {@code CcpMensageriaReceiver}.
 */
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
			process.execute(json);
		}

		return this;
	}

}

package com.ccp.local.testings.implementations;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpJsonFieldName;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpTextDecorator;
import com.ccp.especifications.http.CcpHttpHandler;
import com.ccp.especifications.http.CcpHttpMethods;
import com.ccp.especifications.http.CcpHttpResponseType;

/**
 * Delivery, in its own thread, of a message of {@code PubSubPushEmulator}: a {@code POST} to
 * {@code http://localhost:8083/<topic>} with the body of a Pub/Sub push ({@code {"message": {"data": <message in
 * Base64>}}}), which the local push consumer decodes and runs. The consumer answers when the process ends, so the
 * running mark of the message ({@link CcpLocalMensageriaRunningMarks}) is removed then, with or without success; a
 * failure (the consumer down, or the process failed) goes on to the standard error of the process.
 */
class PubSubPushEmulatorDelivery implements Runnable {

	/** Fields and texts of the push request. */
	enum Fields implements CcpJsonFieldName {
		/** The envelope of the pushed message. */
		message,
		/** The message, in Base64, inside the envelope. */
		data,
		/** The header of the type of the body. */
		Content_Type { public String getValue() { return "Content-Type"; } },
		/** The type of the body. */
		APPLICATION_JSON { public String getValue() { return "application/json"; } },
		/** The address of the local push consumer, followed by the topic. */
		PUSH_CONSUMER_URL { public String getValue() { return "http://localhost:8083/"; } },
		/** The name of the request in the HTTP log. */
		TRACE { public String getValue() { return "pubSubPushEmulator"; } },
	}

	/** The topic (the class name of the process, with its qualifiers). */
	private final String topic;

	/** The message, as JSON text. */
	private final String message;

	/** The name of the running mark of the message. */
	private final String runningMarkName;

	/**
	 * Keeps the message to deliver.
	 * @param topic the topic (the class name of the process, with its qualifiers)
	 * @param message the message, as JSON text
	 * @param runningMarkName the name of the running mark of the message
	 */
	PubSubPushEmulatorDelivery(String topic, String message, String runningMarkName) {
		this.topic = topic;
		this.message = message;
		this.runningMarkName = runningMarkName;
	}

	/** Delivers the message to the push consumer and removes its running mark. */
	public void run() {
		try {
			CcpJsonRepresentation pushRequest = this.getPushRequest();
			String consumerUrl = Fields.PUSH_CONSUMER_URL.getValue();
			String topicUrl = consumerUrl + this.topic;
			String applicationJson = Fields.APPLICATION_JSON.getValue();
			CcpJsonRepresentation headers = CcpOtherConstants.EMPTY_JSON.put(Fields.Content_Type, applicationJson);
			CcpHttpHandler httpHandler = new CcpHttpHandler(200, topicUrl);
			String trace = Fields.TRACE.getValue();
			httpHandler.executeHttpRequest(trace, CcpHttpMethods.POST, headers, pushRequest, CcpHttpResponseType.string);
		} finally {
			CcpLocalMensageriaRunningMarks.unmark(this.runningMarkName);
		}
	}

	/**
	 * The body of a Pub/Sub push: the message, in Base64, under {@code message.data}.
	 * @return the body
	 */
	CcpJsonRepresentation getPushRequest() {
		CcpStringDecorator messageDecorator = new CcpStringDecorator(this.message);
		CcpTextDecorator messageText = messageDecorator.text();
		CcpTextDecorator messageInBase64 = messageText.asBase64();
		CcpJsonRepresentation pushedMessage = CcpOtherConstants.EMPTY_JSON.put(Fields.data, messageInBase64.content);
		CcpJsonRepresentation pushRequest = CcpOtherConstants.EMPTY_JSON.put(Fields.message, pushedMessage);
		return pushRequest;
	}
}

package com.ccp.local.testings.implementations;

import java.io.File;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpFileDecorator;
import com.ccp.decorators.CcpJsonFieldName;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.especifications.instant.messenger.CcpInstantMessenger;

/**
 * {@code CcpInstantMessenger} mock for the local environment. Instead of sending the message through Telegram,
 * it appends it to {@code c:\logs\telegram\<bot>-<chat>.txt}, with date and time. It exists because the API
 * running locally used the real Telegram — every run of the login suite sent real messages to the
 * support chat.
 */
class LocalInstantMessengerFile implements CcpInstantMessenger {

	private static final String FOLDER = "c:\\logs\\telegram";

	public CcpJsonRepresentation sendTextMessage(CcpJsonFieldName botType, String botToken, Long chatId, Long replyTo, String message) {
		this.append(botType, chatId, message);
		return CcpOtherConstants.EMPTY_JSON;
	}

	public CcpJsonRepresentation sendFile(CcpJsonFieldName botType, String botToken, Long chatId, Long replyTo, String fileName, String caption, Byte[] fileContent) {
		String message = "[file " + fileName + "] " + caption;
		this.append(botType, chatId, message);
		return CcpOtherConstants.EMPTY_JSON;
	}

	private void append(CcpJsonFieldName botType, Long chatId, String message) {
		new File(FOLDER).mkdirs();
		String path = FOLDER + "\\" + botType.getValue() + "-" + chatId + ".txt";
		String when = new CcpTimeDecorator().getFormattedDateTime("dd/MM/yyyy HH:mm:ss.SSS");
		CcpFileDecorator file = new CcpStringDecorator(path).file();
		file.append("==== " + when + "\n" + message);
	}
}

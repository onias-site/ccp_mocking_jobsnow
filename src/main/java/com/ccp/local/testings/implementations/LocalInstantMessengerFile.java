package com.ccp.local.testings.implementations;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 * support chat. A sent file is also written, with its content, to {@code c:\logs\telegram\files}.
 */
class LocalInstantMessengerFile implements CcpInstantMessenger {

	private static final String FOLDER = "c:\\logs\\telegram";

	private static final String FILES_FOLDER = FOLDER + "\\files";

	public CcpJsonRepresentation sendTextMessage(CcpJsonFieldName botType, String botToken, Long chatId, Long replyTo, String message) {
		this.append(botType, chatId, message);
		return CcpOtherConstants.EMPTY_JSON;
	}

	public CcpJsonRepresentation sendFile(CcpJsonFieldName botType, String botToken, Long chatId, Long replyTo, String fileName, String caption, Byte[] fileContent) {
		String savedFilePath = this.saveFile(botType, chatId, fileName, fileContent);
		String message = "[file " + savedFilePath + "] " + caption;
		this.append(botType, chatId, message);
		return CcpOtherConstants.EMPTY_JSON;
	}

	/**
	 * Writes the sent file to {@code c:\logs\telegram\files}. The name is prefixed with timestamp, bot and chat so
	 * that sending the same file twice does not overwrite the first one.
	 */
	private String saveFile(CcpJsonFieldName botType, Long chatId, String fileName, Byte[] fileContent) {
		new File(FILES_FOLDER).mkdirs();
		String when = new CcpTimeDecorator().getFormattedDateTime("yyyyMMdd-HHmmss-SSS");
		// characters that Windows does not accept in a file name
		String fileNameValidOnWindows = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
		String path = FILES_FOLDER + "\\" + when + "-" + botType.getValue() + "-" + chatId + "-" + fileNameValidOnWindows;
		byte[] unboxedContent = new byte[fileContent.length];
		for (int index = 0; index < fileContent.length; index++) {
			unboxedContent[index] = fileContent[index];
		}
		Path filePath = Paths.get(path);
		Files.write(filePath, unboxedContent);
		return path;
	}

	private void append(CcpJsonFieldName botType, Long chatId, String message) {
		new File(FOLDER).mkdirs();
		String path = FOLDER + "\\" + botType.getValue() + "-" + chatId + ".txt";
		String when = new CcpTimeDecorator().getFormattedDateTime("dd/MM/yyyy HH:mm:ss.SSS");
		CcpFileDecorator file = new CcpStringDecorator(path).file();
		file.append("==== " + when + "\n" + message); 
	}
}

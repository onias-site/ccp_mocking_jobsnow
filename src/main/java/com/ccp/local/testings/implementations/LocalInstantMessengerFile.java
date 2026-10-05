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

	/** Folder of the message files. */
	private static final String FOLDER = "c:\\logs\\telegram";

	/** Folder of the sent files. */
	private static final String FILES_FOLDER = FOLDER + "\\files";

	/**
	 * Appends the message to the file of the bot and chat.
	 * @param botType the bot type
	 * @param botToken ignored
	 * @param chatId the chat
	 * @param replyTo ignored
	 * @param message the message
	 * @return an empty JSON
	 */
	public CcpJsonRepresentation sendTextMessage(CcpJsonFieldName botType, String botToken, Long chatId, Long replyTo, String message) {
		this.append(botType, chatId, message);
		return CcpOtherConstants.EMPTY_JSON;
	}

	/**
	 * Saves the file and appends its path and caption to the file of the bot and chat.
	 * @param botType the bot type
	 * @param botToken ignored
	 * @param chatId the chat
	 * @param replyTo ignored
	 * @param fileName the file name
	 * @param caption the caption
	 * @param fileContent the file content
	 * @return an empty JSON
	 */
	public CcpJsonRepresentation sendFile(CcpJsonFieldName botType, String botToken, Long chatId, Long replyTo, String fileName, String caption, Byte[] fileContent) {
		String savedFilePath = this.saveFile(botType, chatId, fileName, fileContent);
		String message = "[file " + savedFilePath + "] " + caption;
		this.append(botType, chatId, message);
		return CcpOtherConstants.EMPTY_JSON;
	}

	/**
	 * Writes the sent file to {@code c:\logs\telegram\files}. The name is prefixed with timestamp, bot and chat so that
	 * sending the same file twice does not overwrite the first one.
	 * @param botType the bot type
	 * @param chatId the chat
	 * @param fileName the file name
	 * @param fileContent the file content
	 * @return the path written
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

	/**
	 * Appends {@code ==== <date time>} and the message to {@code c:\logs\telegram\<bot>-<chat>.txt}.
	 * @param botType the bot type
	 * @param chatId the chat
	 * @param message the message
	 */
	private void append(CcpJsonFieldName botType, Long chatId, String message) {
		new File(FOLDER).mkdirs();
		String path = FOLDER + "\\" + botType.getValue() + "-" + chatId + ".txt";
		String when = new CcpTimeDecorator().getFormattedDateTime("dd/MM/yyyy HH:mm:ss.SSS");
		CcpFileDecorator file = new CcpStringDecorator(path).file();
		file.append("==== " + when + "\n" + message); 
	}
}

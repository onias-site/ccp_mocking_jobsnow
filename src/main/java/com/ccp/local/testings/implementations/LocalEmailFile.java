package com.ccp.local.testings.implementations;

import java.util.ArrayList;
import java.util.List;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpJsonFieldName;
import com.ccp.especifications.email.CcpEmailProvider;
import com.ccp.especifications.email.CcpEmailSuppressionList;
import com.ccp.especifications.http.CcpHttpContentType;
import com.ccp.decorators.CcpFileDecorator;

/**
 * {@code CcpEmailProvider} mock for local tests. Instead of sending the e-mail, it writes the
 * content to {@code c:\logs\email\<templateId>.html}.
 */
class LocalEmailFile implements CcpEmailProvider {
	/** The single instance: the class keeps no state. */
	public final static LocalEmailFile INSTANCE = new LocalEmailFile();

	private LocalEmailFile() {}

	/** Unused. */
	enum JsonFieldNames implements CcpJsonFieldName{
		/** Unused. */
		templateId
	}

	/**
	 * Writes the message to {@code c:\logs\email\<templateId>.html}, replacing the previous one, and also to
	 * {@code c:\logs\email\<recipient>\<templateId>.html} for each recipient. The shared file is overwritten by every
	 * sending; the file of the recipient lets parallel automations read each one its own message (the login token, for
	 * instance) without picking up another's. Synchronized: two sendings at once raced between the reset and the write of
	 * the same file.
	 * @param providerToken ignored
	 * @param providerUrl ignored
	 * @param templateId the file name
	 * @param sender ignored
	 * @param subject ignored
	 * @param message the content written
	 * @param contentType ignored
	 * @param emails the recipients, each one with its own copy
	 * @return an empty JSON
	 */
	public synchronized CcpJsonRepresentation sendSimpleTextEmailMessage(String providerToken, String providerUrl, String templateId, String sender, String subject, String message, CcpHttpContentType contentType, String... emails){
		String emailFileName = templateId + ".html";
		this.write("c:\\logs\\email\\" + emailFileName, message);
		for (String email : emails) {
			this.write("c:\\logs\\email\\" + email + "\\" + emailFileName, message);
		}
		return CcpOtherConstants.EMPTY_JSON;
	}

	/**
	 * Nothing is delivered in the local environment, so nothing is ever suppressed.
	 * @param providerToken ignored
	 * @param providerUrl ignored
	 * @param suppressionList ignored
	 * @return an empty list
	 */
	public List<CcpJsonRepresentation> getSuppressedEmails(String providerToken, String providerUrl, CcpEmailSuppressionList suppressionList) {
		return new ArrayList<>();
	}

	/**
	 * Nothing is delivered in the local environment, so nothing is ever suppressed.
	 * @param providerToken ignored
	 * @param providerUrl ignored
	 * @param suppressionList ignored
	 * @param startTimeInSeconds ignored
	 * @return an empty list
	 */
	public List<CcpJsonRepresentation> getSuppressedEmailsCreatedSince(String providerToken, String providerUrl, CcpEmailSuppressionList suppressionList, long startTimeInSeconds) {
		return new ArrayList<>();
	}

	/**
	 * Replaces the content of the file with the message.
	 * @param emailFilePath the file
	 * @param message the content written
	 */
	private void write(String emailFilePath, String message) {
		CcpStringDecorator emailFilePathDecorator = new CcpStringDecorator(emailFilePath);
		CcpFileDecorator emailFileDecorator = emailFilePathDecorator.file();
		var emailFile = emailFileDecorator.reset();
		emailFile.append(message);
	}

}

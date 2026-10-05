package com.ccp.local.testings.implementations;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpJsonFieldName;
import com.ccp.especifications.email.CcpEmailSender;
import com.ccp.especifications.http.CcpHttpContentType;
import com.ccp.decorators.CcpFileDecorator;

/**
 * {@code CcpEmailSender} mock for local tests. Instead of sending the e-mail, it writes the
 * content to {@code c:\logs\email\<templateId>.html}.
 */
class LocalEmailFile implements CcpEmailSender {
	/** Unused. */
	enum JsonFieldNames implements CcpJsonFieldName{
		/** Unused. */
		templateId
	}

	/**
	 * Writes the message to {@code c:\logs\email\<templateId>.html}, replacing the previous one.
	 * @param providerToken ignored
	 * @param providerUrl ignored
	 * @param templateId the file name
	 * @param sender ignored
	 * @param subject ignored
	 * @param message the content written
	 * @param contentType ignored
	 * @param emails ignored
	 * @return an empty JSON
	 */
	public CcpJsonRepresentation sendSimpleTextEmailMessage(String providerToken, String providerUrl, String templateId, String sender, String subject, String message, CcpHttpContentType contentType, String... emails){
		String emailFilePathWithoutExtension = "c:\\logs\\email\\" + templateId;
		String emailFilePath = emailFilePathWithoutExtension + ".html";
		CcpStringDecorator emailFilePathDecorator = new CcpStringDecorator(emailFilePath);
		CcpFileDecorator emailFileDecorator = emailFilePathDecorator.file();
		var emailFile = emailFileDecorator.reset();
		emailFile.append(message);
		return CcpOtherConstants.EMPTY_JSON;
	}

}

package com.ccp.local.testings.implementations;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpJsonRepresentation.CcpJsonFieldName;
import com.ccp.especifications.email.CcpEmailSender;
import com.ccp.especifications.http.CcpHttpContentType;

/**
 * Mock de {@code CcpEmailSender} para testes locais. Em vez de enviar e-mail, persiste o
 * conteúdo em {@code c:\logs\email\<templateId>.html}.
 */
class LocalEmailFile implements CcpEmailSender {
	enum JsonFieldNames implements CcpJsonFieldName{
		templateId
	}

	public CcpJsonRepresentation sendSimpleTextEmailMessage(String providerToken, String providerUrl, String templateId, String sender, String subject, String message, CcpHttpContentType contentType, String... emails){
		new CcpStringDecorator("c:\\logs\\email\\" + templateId + ".html").file().reset().append(message);
		return CcpOtherConstants.EMPTY_JSON;
	}

}

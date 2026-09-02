package com.ccp.local.testings.implementations;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpJsonFieldName;
import com.ccp.especifications.email.CcpEmailSender;
import com.ccp.especifications.http.CcpHttpContentType;
import com.ccp.decorators.CcpFileDecorator;

/**
 * Mock de {@code CcpEmailSender} para testes locais. Em vez de enviar e-mail, persiste o
 * conteúdo em {@code c:\logs\email\<templateId>.html}.
 */
class LocalEmailFile implements CcpEmailSender {
	enum JsonFieldNames implements CcpJsonFieldName{
		templateId
	}

	public CcpJsonRepresentation sendSimpleTextEmailMessage(String providerToken, String providerUrl, String templateId, String sender, String subject, String message, CcpHttpContentType contentType, String... emails){
		String valorMais = "c:\\logs\\email\\" + templateId;
		String valorMaisMais = valorMais + ".html";
		CcpStringDecorator ccpStringDecorator = new CcpStringDecorator(valorMaisMais);
		CcpFileDecorator ccpStringDecoratorFile = ccpStringDecorator.file();
		var reset = ccpStringDecoratorFile.reset();
		reset.append(message);
		return CcpOtherConstants.EMPTY_JSON;
	}

}

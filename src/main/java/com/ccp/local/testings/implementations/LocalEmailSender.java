package com.ccp.local.testings.implementations;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpJsonRepresentation.CcpJsonFieldName;
import com.ccp.especifications.email.CcpEmailSender;

class LocalEmailSender implements CcpEmailSender {
	enum JsonFieldNames implements CcpJsonFieldName{
		templateId
	}

	public CcpJsonRepresentation send(CcpJsonRepresentation emailApiParameters) {
		String templateId = emailApiParameters.getAsString(JsonFieldNames.templateId);
		new CcpStringDecorator("c:\\logs\\email\\" + templateId + ".json").file().reset().append(emailApiParameters.asPrettyJson());
		return emailApiParameters;
	}

}

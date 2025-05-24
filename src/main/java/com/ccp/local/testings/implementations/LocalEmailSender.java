package com.ccp.local.testings.implementations;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.especifications.email.CcpEmailSender;

public class LocalEmailSender implements CcpEmailSender {

	public CcpJsonRepresentation send(CcpJsonRepresentation emailApiParameters) {
		String templateId = emailApiParameters.getAsString("templateId");
		new CcpStringDecorator("c:\\logs\\email\\" + templateId + ".json").file().reset().append(emailApiParameters.asPrettyJson());
		return emailApiParameters;
	}

}

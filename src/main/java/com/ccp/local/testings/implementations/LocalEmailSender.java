package com.ccp.local.testings.implementations;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpJsonRepresentation.CcpJsonFieldName;
import com.ccp.especifications.email.CcpEmailSender;
enum LocalEmailSenderConstants  implements CcpJsonFieldName{
	templateId
	
}

class LocalEmailSender implements CcpEmailSender {

	public CcpJsonRepresentation send(CcpJsonRepresentation emailApiParameters) {
		String templateId = emailApiParameters.getAsString(LocalEmailSenderConstants.templateId);
		new CcpStringDecorator("c:\\logs\\email\\" + templateId + ".json").file().reset().append(emailApiParameters.asPrettyJson());
		return emailApiParameters;
	}

}

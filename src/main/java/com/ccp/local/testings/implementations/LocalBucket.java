package com.ccp.local.testings.implementations;

import com.ccp.decorators.CcpFileDecorator;
import com.ccp.decorators.CcpFolderDecorator;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.especifications.file.bucket.CcpFileBucket;

class LocalBucket implements CcpFileBucket{

	public String get(String tenant, String bucketName, String fileName) {
		CcpFileDecorator file = this.getFile(bucketName, fileName);
		String extractStringContent = file.getStringContent();
		return extractStringContent;
	}

	public String delete(String tenant, String bucketName, String fileName) {
		CcpFileDecorator file = this.getFile(bucketName, fileName);
		file.remove();
		return "";
	}

	public String save(String tenant, String bucketName, String fileName, String fileContent) {
		CcpFileDecorator file2 = this.getFile(bucketName, fileName);
		CcpFileDecorator file = file2.reset();
		file.append(fileContent);
		return fileContent;
	}

	private CcpFileDecorator getFile(String bucketName, String fileName) {
		String content = "c:/logs/" + bucketName + "/" + fileName;
		CcpStringDecorator ccpStringDecorator = new CcpStringDecorator(content);
		CcpFileDecorator file = ccpStringDecorator.file();
		return file;
	}

	public String delete(String tenant, String bucketName) {
		String content = "c:/logs/" + bucketName;
		CcpStringDecorator ccpStringDecorator = new CcpStringDecorator(content);
		CcpFolderDecorator folder = ccpStringDecorator.folder();
		folder.remove();
		return "";
	}
}

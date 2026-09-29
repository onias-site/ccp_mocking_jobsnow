package com.ccp.local.testings.implementations;

import com.ccp.decorators.CcpFileDecorator;
import com.ccp.decorators.CcpFolderDecorator;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.especifications.file.bucket.CcpFileBucket;

/**
 * {@code CcpFileBucket} mock for local tests. Stores and retrieves files at
 * {@code c:/logs/<bucketName>/<fileName>} on the local file system.
 */
class LocalBucket implements CcpFileBucket{

	public String get(String tenant, String bucketName, String fileName) {
		CcpFileDecorator file = this.getFile(bucketName, fileName);
		String fileContent = file.getStringContent();
		return fileContent;
	}

	public String delete(String tenant, String bucketName, String fileName) {
		CcpFileDecorator file = this.getFile(bucketName, fileName);
		file.remove();
		return "";
	}

	public String save(String tenant, String bucketName, String fileName, String fileContent) {
		CcpFileDecorator existingFile = this.getFile(bucketName, fileName);
		CcpFileDecorator file = existingFile.reset();
		file.append(fileContent);
		return fileContent;
	}

	private CcpFileDecorator getFile(String bucketName, String fileName) {
		String bucketFolder = "c:/logs/" + bucketName;
		String bucketFolderWithSlash = bucketFolder + "/";
		String content = bucketFolderWithSlash + fileName;
		CcpStringDecorator pathDecorator = new CcpStringDecorator(content);
		CcpFileDecorator file = pathDecorator.file();
		return file;
	}

	public String delete(String tenant, String bucketName) {
		String content = "c:/logs/" + bucketName;
		CcpStringDecorator pathDecorator = new CcpStringDecorator(content);
		CcpFolderDecorator folder = pathDecorator.folder();
		folder.remove();
		return "";
	}
}

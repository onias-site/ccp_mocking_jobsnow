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

	/**
	 * Reads {@code c:/logs/<bucket>/<file>}.
	 * @param tenant ignored
	 * @param bucketName the bucket folder
	 * @param fileName the file
	 * @return the file content
	 */
	public String get(String tenant, String bucketName, String fileName) {
		CcpFileDecorator file = this.getFile(bucketName, fileName);
		String fileContent = file.getStringContent();
		return fileContent;
	}

	/**
	 * Deletes {@code c:/logs/<bucket>/<file>}.
	 * @param tenant ignored
	 * @param bucketName the bucket folder
	 * @param fileName the file
	 * @return an empty text
	 */
	public String delete(String tenant, String bucketName, String fileName) {
		CcpFileDecorator file = this.getFile(bucketName, fileName);
		file.remove();
		return "";
	}

	/**
	 * Writes {@code c:/logs/<bucket>/<file>} (the content followed by a line feed).
	 * @param tenant ignored
	 * @param bucketName the bucket folder
	 * @param fileName the file
	 * @param fileContent the content
	 * @return the content
	 */
	public String save(String tenant, String bucketName, String fileName, String fileContent) {
		CcpFileDecorator existingFile = this.getFile(bucketName, fileName);
		CcpFileDecorator file = existingFile.reset();
		file.append(fileContent);
		return fileContent;
	}

	/**
	 * Returns the file {@code c:/logs/<bucket>/<file>}.
	 * @param bucketName the bucket folder
	 * @param fileName the file
	 * @return the file decorator
	 */
	private CcpFileDecorator getFile(String bucketName, String fileName) {
		String bucketFolder = "c:/logs/" + bucketName;
		String bucketFolderWithSlash = bucketFolder + "/";
		String content = bucketFolderWithSlash + fileName;
		CcpStringDecorator pathDecorator = new CcpStringDecorator(content);
		CcpFileDecorator file = pathDecorator.file();
		return file;
	}

	/**
	 * Deletes the folder {@code c:/logs/<bucket>} (only its direct files, see {@code CcpFolderDecorator.remove}).
	 * @param tenant ignored
	 * @param bucketName the bucket folder
	 * @return an empty text
	 */
	public String delete(String tenant, String bucketName) {
		String content = "c:/logs/" + bucketName;
		CcpStringDecorator pathDecorator = new CcpStringDecorator(content);
		CcpFolderDecorator folder = pathDecorator.folder();
		folder.remove();
		return "";
	}
}

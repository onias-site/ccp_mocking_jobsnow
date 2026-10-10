package com.ccp.local.testings.implementations.cache;

import com.ccp.decorators.CcpFileDecorator;
import com.ccp.decorators.CcpHashDecorator;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.hash.CcpHashAlgorithm;

/**
 * Deletions of cache keys shared by the local processes (the REST APIs, the support bot listener). Locally each process
 * has its own memory, while in production the cache is shared by all of them, so a key dropped by one process is dropped
 * for every one. To behave the same, every deletion leaves a mark with its time in a folder every local process sees, one
 * file per key (named by the hash of the key), and an entry stored before the mark of its key is a miss.
 */
final class CacheDeletionMarks {

	/** The folder of the deletion marks. */
	private static final String DELETIONS_FOLDER = "c:/logs/cache/deleted-keys/";

	/** Utility class; not instantiable. */
	private CacheDeletionMarks() {}

	/**
	 * Leaves the mark of the deletion of the key, with the current time.
	 * @param key the cache key
	 */
	static void markDeletion(String key) {
		CcpFileDecorator deletionMark = getDeletionMark(key);
		long now = System.currentTimeMillis();
		String deletionTime = String.valueOf(now);
		deletionMark.write(deletionTime);
	}

	/**
	 * Tells whether the key was deleted, by any local process, at or after the given time.
	 * @param key the cache key
	 * @param storedTime when the entry was stored
	 * @return {@code true} when the entry stored at that time is no longer valid
	 */
	static boolean wasDeletedAfter(String key, long storedTime) {
		CcpFileDecorator deletionMark = getDeletionMark(key);
		boolean noDeletion = false == deletionMark.exists();
		if(noDeletion) {
			return false;
		}
		String deletionTimeText = deletionMark.getStringContent();
		String trimmedDeletionTime = deletionTimeText.trim();
		long deletionTime = Long.parseLong(trimmedDeletionTime);
		boolean deletedAfter = deletionTime >= storedTime;
		return deletedAfter;
	}

	/**
	 * The file of the deletion mark of the key.
	 * @param key the cache key
	 * @return the file
	 */
	private static CcpFileDecorator getDeletionMark(String key) {
		CcpStringDecorator keyDecorator = new CcpStringDecorator(key);
		CcpHashDecorator keyHashDecorator = keyDecorator.hash();
		String keyHash = keyHashDecorator.asString(CcpHashAlgorithm.SHA1);
		String deletionMarkPath = DELETIONS_FOLDER + keyHash;
		CcpStringDecorator deletionMarkPathDecorator = new CcpStringDecorator(deletionMarkPath);
		CcpFileDecorator deletionMark = deletionMarkPathDecorator.file();
		return deletionMark;
	}
}

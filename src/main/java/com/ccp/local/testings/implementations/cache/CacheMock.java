package com.ccp.local.testings.implementations.cache;

import com.ccp.aop.CcpAllowNullReturn;
import com.ccp.especifications.cache.CcpCache;

/**
 * Null {@code CcpCache} implementation for tests where the cache must be ignored.
 * It stores nothing; a deletion only leaves the mark that drops the key in the other local processes ({@link CacheDeletionMarks}).
 */
class CacheMock implements CcpCache {

	/**
	 * Always a miss.
	 * @param key the cache key
	 * @return always {@code null}
	 */
	@CcpAllowNullReturn
	public Object get(String key) {
		return null;
	}

	/**
	 * Stores nothing.
	 * @param key the cache key
	 * @param value the value
	 * @param secondsDelay the expiration
	 * @return this cache
	 */
	public CcpCache put(String key, Object value, int secondsDelay) {
		return this;
	}

	/**
	 * Leaves the deletion mark of the key for the other local processes.
	 * @param key the cache key
	 */
	public void delete(String key) {
		// stores nothing, but the other local processes may: the support bot listener uses this cache, and the
		// approvals it runs must drop the entries the REST APIs keep
		CacheDeletionMarks.markDeletion(key);
	}

}

package com.ccp.local.testings.implementations.cache;

import com.ccp.aop.CcpAllowNullReturn;
import com.ccp.especifications.cache.CcpCache;

/**
 * Null {@code CcpCache} implementation for tests where the cache must be ignored.
 * Every method returns {@code null} or {@code this} with no side effects.
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
	 * Does nothing.
	 * @param key the cache key
	 */
	public void delete(String key) {
	}

}

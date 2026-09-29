package com.ccp.local.testings.implementations.cache;

import com.ccp.aop.CcpAllowNullReturn;
import com.ccp.especifications.cache.CcpCache;

/**
 * Null {@code CcpCache} implementation for tests where the cache must be ignored.
 * Every method returns {@code null} or {@code this} with no side effects.
 */
class CacheMock implements CcpCache {

	@CcpAllowNullReturn
	public Object get(String key) {
		return null;
	}

	public CcpCache put(String key, Object value, int secondsDelay) {
		return this;
	}

	public void delete(String key) {
	}

}

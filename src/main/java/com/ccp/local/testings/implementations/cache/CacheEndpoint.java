package com.ccp.local.testings.implementations.cache;

import com.ccp.especifications.cache.CcpCache;

class CacheEndpoint implements CcpCache {

	public Object get(String key) {
		// DOUBT
		return null;
	}

	public CcpCache put(String key, Object value, int secondsDelay) {
		// DOUBT
		return this;
	}

	public <V> V delete(String key) {
		// DOUBT
		return null;
	}

}

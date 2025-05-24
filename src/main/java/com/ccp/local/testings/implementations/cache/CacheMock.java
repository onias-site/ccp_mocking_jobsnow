package com.ccp.local.testings.implementations.cache;

import com.ccp.especifications.cache.CcpCache;

class CacheMock implements CcpCache {

	public Object get(String key) {
		return null;
	}

	public CcpCache put(String key, Object value, int secondsDelay) {
		return this;
	}

	public <V> V delete(String key) {
		return null;
	}

}

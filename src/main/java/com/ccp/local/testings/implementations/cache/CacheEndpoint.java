package com.ccp.local.testings.implementations.cache;

import com.ccp.especifications.cache.CcpCache;

/**
 * Implementação stub de {@code CcpCache} que delega para um endpoint externo (ainda não
 * implementada). Todos os métodos retornam {@code null} ou {@code this}.
 */
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

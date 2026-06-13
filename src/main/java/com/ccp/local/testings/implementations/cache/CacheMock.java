package com.ccp.local.testings.implementations.cache;

import com.ccp.especifications.cache.CcpCache;

/**
 * Implementação nula de {@code CcpCache} para testes onde o cache deve ser ignorado.
 * Todos os métodos retornam {@code null} ou {@code this} sem efeito colateral.
 */
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

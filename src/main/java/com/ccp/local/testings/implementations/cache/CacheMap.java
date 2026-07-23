package com.ccp.local.testings.implementations.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpFieldName;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.especifications.cache.CcpCache;

/**
 * Implementação in-memory de {@code CcpCache} para testes locais. Mantém um mapa estático
 * compartilhado com suporte a expiração por tempo ({@code secondsDelay}).
 */
class CacheMap implements CcpCache {
	
	private static CcpJsonRepresentation localCache = CcpOtherConstants.EMPTY_JSON;

	@SuppressWarnings("unchecked")
	public synchronized Object get(String key) {

		boolean itIsMissingFields = false == localCache.containsAllFields(new CcpFieldName(key));
		if(itIsMissingFields) {
			return null;
		}

		Object object = localCache.get(new CcpFieldName(key));

		if(object instanceof Map map) {
			CcpJsonRepresentation jr = new CcpJsonRepresentation(map);
			return jr;
		}
		return object;
	}



	public CcpCache put(String key, Object value, int secondsDelay) {

		if(value instanceof CcpJsonRepresentation json) {
			value = new LinkedHashMap<>(json.content);
		}
		localCache = localCache.put(new CcpFieldName(key), value);
		new CcpTimeDecorator().sleep(1);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <V> V delete(String key) {
		
		V t = (V) this.get(key);
		
		CcpFieldName field = new CcpFieldName(key);
		localCache = localCache.removeFields(field);

		return t;
	}

}

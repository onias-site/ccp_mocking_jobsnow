package com.ccp.local.testings.implementations.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ccp.aop.CcpAllowNullReturn;
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
	@CcpAllowNullReturn
	public synchronized Object get(String key) {
		CcpFieldName ccpFieldName = new CcpFieldName(key);
		boolean containsAllFields = localCache.containsAllFields(ccpFieldName);

		boolean itIsMissingFields = false == containsAllFields;
		if(itIsMissingFields) {
			return null;
		}
		CcpFieldName ccpFieldName2 = new CcpFieldName(key);

		Object object = localCache.get(ccpFieldName2);

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
		CcpFieldName ccpFieldName3 = new CcpFieldName(key);
		localCache = localCache.put(ccpFieldName3, value);
		CcpTimeDecorator ccpTimeDecorator = new CcpTimeDecorator();
		ccpTimeDecorator.sleep(1);
		return this;
	}

	@CcpAllowNullReturn
	@SuppressWarnings("unchecked")
	public <V> V delete(String key) {
		var get = this.get(key);
	
		V t = (V) get;
		
		CcpFieldName field = new CcpFieldName(key);
		localCache = localCache.removeFields(field);

		return t;
	}

}

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
 * In-memory {@code CcpCache} implementation for local tests. Keeps a shared static map
 * with support for time-based expiration ({@code secondsDelay}).
 */
class CacheMap implements CcpCache {
	
	private static CcpJsonRepresentation localCache = CcpOtherConstants.EMPTY_JSON;

	@SuppressWarnings("unchecked")
	@CcpAllowNullReturn
	public synchronized Object get(String key) {
		CcpFieldName keyField = new CcpFieldName(key);
		boolean containsAllFields = localCache.containsAllFields(keyField);

		boolean itIsMissingFields = false == containsAllFields;
		if(itIsMissingFields) {
			return null;
		}
		CcpFieldName keyToRead = new CcpFieldName(key);

		Object object = localCache.get(keyToRead);

		if(object instanceof Map map) {
			CcpJsonRepresentation cachedJson = new CcpJsonRepresentation(map);
			return cachedJson;
		}
		return object;
	}



	public CcpCache put(String key, Object value, int secondsDelay) {

		if(value instanceof CcpJsonRepresentation json) {
			value = new LinkedHashMap<>(json.content);
		}
		CcpFieldName keyField = new CcpFieldName(key);
		localCache = localCache.put(keyField, value);
		CcpTimeDecorator ccpTimeDecorator = new CcpTimeDecorator();
		ccpTimeDecorator.sleep(1);
		return this;
	}

	public synchronized void delete(String key) {
		CcpFieldName field = new CcpFieldName(key);
		localCache = localCache.removeFields(field);
	}

}

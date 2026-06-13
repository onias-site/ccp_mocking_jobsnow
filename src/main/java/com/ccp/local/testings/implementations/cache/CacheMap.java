package com.ccp.local.testings.implementations.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.decorators.CcpFieldName;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.especifications.cache.CcpCache;

/**
 * Implementação in-memory de {@code CcpCache} para testes locais. Mantém um mapa estático
 * compartilhado com suporte a expiração por tempo ({@code secondsDelay}).
 */
class CacheMap implements CcpCache {
	
	private static CcpJsonRepresentation expirations = CcpOtherConstants.EMPTY_JSON;
	private static CcpJsonRepresentation localCache = CcpOtherConstants.EMPTY_JSON;

	@SuppressWarnings("unchecked")
	public synchronized Object get(String key) {

		boolean itIsMissingFields = false == localCache.containsAllFields(new CcpFieldName(key));
		if(itIsMissingFields) {
			return null;
		}

		boolean expiredKey = this.isExpiredKey(key);
		
		if(expiredKey) {
			return null;
		}
		
		Object object = localCache.get(new CcpFieldName(key));

		if(object instanceof Map map) {
			CcpJsonRepresentation jr = new CcpJsonRepresentation(map);
			return jr;
		}
		return object;
	}


	private boolean isExpiredKey(String key) {
		List<String> collect = expirations.fieldSet().stream()
		.filter(x -> System.currentTimeMillis() >= Long.valueOf(x))
		.collect(Collectors.toList());

		for (String time : collect) {
			
			List<String> expiredKeys = expirations.getAsStringList(new CcpFieldName(time));
			
			boolean thisKeyIsNotExpired = false == expiredKeys.contains(key);
			
			if(thisKeyIsNotExpired) {
				continue;
			}
			
			localCache = localCache.removeFields(new CcpFieldName(key));
			expirations = expirations.removeFields(new CcpFieldName(time));
			return true;
		}
		
		return false;
	}


	public CcpCache put(String key, Object value, int secondsDelay) {

		if(value instanceof CcpJsonRepresentation json) {
			value = new LinkedHashMap<>(json.content);
		}
		localCache = localCache.put(new CcpFieldName(key), value);
		long expiration = System.currentTimeMillis() + (secondsDelay * 1000);
		expirations = expirations.addToList(new CcpFieldName("" + expiration), key);
		new CcpTimeDecorator().sleep(1);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <V> V delete(String key) {
		
		V t = (V) this.get(key);
		
		localCache = localCache.removeFields(new CcpFieldName(key));
		Optional<String> findFirst = expirations.fieldSet().stream()
		.filter(x -> expirations.getAsString(new CcpFieldName(x)).equals(key)).findFirst();

		if(findFirst.isPresent()) {
			String expirationToRemove = findFirst.get();
			expirations = expirations.removeFields(new CcpFieldName(expirationToRemove));
		}
		
		return t;
	}

}

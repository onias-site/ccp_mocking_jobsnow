package com.ccp.local.testings.implementations.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.especifications.cache.CcpCache;
import com.ccp.especifications.cache.CcpCacheDecorator;

class CacheMap implements CcpCache {
	
	private static CcpJsonRepresentation expirations = CcpOtherConstants.EMPTY_JSON;
	private static CcpJsonRepresentation localCache = CcpOtherConstants.EMPTY_JSON;

	@SuppressWarnings("unchecked")
	public synchronized Object get(String key) {

		boolean itIsMissingFields = false == localCache.getDynamicVersion().containsAllFields(key);
		if(itIsMissingFields) {
			return null;
		}

		boolean expiredKey = this.isExpiredKey(key);
		
		if(expiredKey) {
			return null;
		}
		
		Object object = localCache.getDynamicVersion().get(key);

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
			
			List<String> expiredKeys = expirations.getDynamicVersion().getAsStringList(time);
			
			boolean thisKeyIsNotExpired = false == expiredKeys.contains(key);
			
			if(thisKeyIsNotExpired) {
				continue;
			}
			
			localCache = localCache.getDynamicVersion().removeField(key);
			expirations = expirations.getDynamicVersion().removeField(time);
			return true;
		}
		
		return false;
	}


	public CcpCache put(String key, Object value, int secondsDelay) {

		if(value instanceof CcpJsonRepresentation json) {
			value = new LinkedHashMap<>(json.content);
		}
		localCache = localCache.getDynamicVersion().put(key, value);
		long expiration = System.currentTimeMillis() + (secondsDelay * 1000);
		expirations = expirations.getDynamicVersion().addToList("" + expiration, key);
		new CcpTimeDecorator().sleep(1);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <V> V delete(String key) {
		
		V t = (V) this.get(key);
		
		localCache = localCache.getDynamicVersion().removeField(key);
		Optional<String> findFirst = expirations.fieldSet().stream()
		.filter(x -> expirations.getDynamicVersion().getAsString(x).equals(key)).findFirst();
		
		if(findFirst.isPresent()) {
			String expirationToRemove = findFirst.get();
			expirations = expirations.getDynamicVersion().removeField(expirationToRemove);
		}
		
		return t;
	}

}

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
 * In-memory {@code CcpCache} for local tests, backed by a static JSON shared by every instance. The expiration is ignored:
 * entries live until deleted, here or in another local process ({@link CacheDeletionMarks}). Until 2026-10-08 a deletion
 * in another process never reached this memory: the approval of a skill, which runs in the support bot listener and drops
 * the cached group, did not change the screen until the vis API was restarted.
 */
class CacheMap implements CcpCache {

	/** The cached entries, by key. */
	private static CcpJsonRepresentation localCache = CcpOtherConstants.EMPTY_JSON;

	/** When each entry was stored, by key. */
	private static final Map<String, Long> storedAt = new LinkedHashMap<>();

	/**
	 * Returns the cached value, rebuilding a cached map as {@code CcpJsonRepresentation}; an entry deleted by another
	 * process after it was stored here is dropped and is a miss.
	 * @param key the cache key
	 * @return the value, or {@code null} on a miss
	 */
	@SuppressWarnings("unchecked")
	@CcpAllowNullReturn
	public synchronized Object get(String key) {
		CcpFieldName keyField = new CcpFieldName(key);
		boolean containsAllFields = localCache.containsAllFields(keyField);

		boolean itIsMissingFields = false == containsAllFields;
		if(itIsMissingFields) {
			return null;
		}

		long storedTime = storedAt.getOrDefault(key, 0L);
		boolean deletedByAnotherProcess = CacheDeletionMarks.wasDeletedAfter(key, storedTime);
		if(deletedByAnotherProcess) {
			localCache = localCache.removeFields(keyField);
			storedAt.remove(key);
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

	/**
	 * Stores the value (a JSON as a copy of its map), ignoring the expiration, and sleeps one millisecond. Unlike
	 * {@code get} and {@code delete}, it is not synchronized.
	 * @param key the cache key
	 * @param value the value
	 * @param secondsDelay ignored
	 * @return this cache
	 */
	public CcpCache put(String key, Object value, int secondsDelay) {

		if(value instanceof CcpJsonRepresentation json) {
			value = new LinkedHashMap<>(json.content);
		}
		CcpFieldName keyField = new CcpFieldName(key);
		localCache = localCache.put(keyField, value);
		long now = System.currentTimeMillis();
		storedAt.put(key, now);
		CcpTimeDecorator ccpTimeDecorator = new CcpTimeDecorator();
		ccpTimeDecorator.sleep(1);
		return this;
	}

	/**
	 * Removes the key here and in the other local processes.
	 * @param key the cache key
	 */
	public synchronized void delete(String key) {
		CcpFieldName field = new CcpFieldName(key);
		localCache = localCache.removeFields(field);
		storedAt.remove(key);
		CacheDeletionMarks.markDeletion(key);
	}

}

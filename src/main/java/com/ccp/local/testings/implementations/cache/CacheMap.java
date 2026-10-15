package com.ccp.local.testings.implementations.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ccp.aop.CcpAllowNullReturn;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.especifications.cache.CcpCache;

/**
 * In-memory {@code CcpCache} for local tests, backed by a static map shared by every instance. The expiration is ignored:
 * entries live until deleted, here or in another local process ({@link CacheDeletionMarks}). Until 2026-10-08 a deletion
 * in another process never reached this memory: a key dropped by one process stayed cached in the others until they
 * were restarted.
 *
 * <p>Until 2026-10-14 the entries lived in an immutable {@code CcpJsonRepresentation}: every put and every delete copied
 * the whole cache, under a lock shared by every key. With the groups of the resumes (hundreds of keys per save) the cost
 * grew with the square of the cache, and a save of a resume with 8 groups took 12 s while other saves ran.
 */
class CacheMap implements CcpCache {

	/** The cached entries, by key. */
	private static final Map<String, Object> localCache = new ConcurrentHashMap<>();

	/** When each entry was stored, by key. */
	private static final Map<String, Long> storedAt = new ConcurrentHashMap<>();

	/**
	 * Returns the cached value, rebuilding a cached map as {@code CcpJsonRepresentation}; an entry deleted by another
	 * process after it was stored here is dropped and is a miss.
	 * @param key the cache key
	 * @return the value, or {@code null} on a miss
	 */
	@SuppressWarnings("unchecked")
	@CcpAllowNullReturn
	public Object get(String key) {
		Object object = localCache.get(key);

		boolean missing = object == null;
		if(missing) {
			return null;
		}

		long storedTime = storedAt.getOrDefault(key, 0L);
		boolean deletedByAnotherProcess = CacheDeletionMarks.wasDeletedAfter(key, storedTime);
		if(deletedByAnotherProcess) {
			localCache.remove(key);
			storedAt.remove(key);
			return null;
		}

		if(object instanceof Map map) {
			CcpJsonRepresentation cachedJson = new CcpJsonRepresentation(map);
			return cachedJson;
		}
		return object;
	}

	/**
	 * Stores the value (a JSON as a copy of its map), ignoring the expiration, and sleeps one millisecond.
	 * @param key the cache key
	 * @param value the value
	 * @param secondsDelay ignored
	 * @return this cache
	 */
	public CcpCache put(String key, Object value, int secondsDelay) {

		if(value instanceof CcpJsonRepresentation json) {
			value = new LinkedHashMap<>(json.content);
		}
		localCache.put(key, value);
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
	public void delete(String key) {
		localCache.remove(key);
		storedAt.remove(key);
		CacheDeletionMarks.markDeletion(key);
	}

}

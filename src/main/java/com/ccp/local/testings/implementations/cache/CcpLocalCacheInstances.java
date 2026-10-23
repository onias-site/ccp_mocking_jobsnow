package com.ccp.local.testings.implementations.cache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

/** Cache providers for local tests: {@code map} (in-memory) and {@code mock} (always a miss). */
public enum CcpLocalCacheInstances implements CcpInstanceProvider<CcpCache>{

	/** In-memory cache shared by the whole JVM. */
	map{

		/**
		 * Builds the in-memory cache.
		 * @return a new {@code CacheMap}
		 */
		public CcpCache getInstance() {
			CacheMap cacheMap = CacheMap.INSTANCE;
			return cacheMap;
		}
	},
	
	/** Cache that stores nothing. */
	mock {

		/**
		 * Builds the cache that stores nothing.
		 * @return a new {@code CacheMock}
		 */
		public CcpCache getInstance() {
			CacheMock cacheMock = CacheMock.INSTANCE;
			return cacheMock;
		}
	}
	;

}	

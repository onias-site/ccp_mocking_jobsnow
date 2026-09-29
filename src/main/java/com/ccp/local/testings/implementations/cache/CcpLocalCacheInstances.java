package com.ccp.local.testings.implementations.cache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

/**
 * Enum of cache DI providers for local tests. Offers three variants: {@code map}
 * (in-memory with expiration), {@code endpoint} (external endpoint stub) and {@code mock} (no-op).
 */
public enum CcpLocalCacheInstances implements CcpInstanceProvider<CcpCache>{

	map{

		public CcpCache getInstance() {
			CacheMap cacheMap = new CacheMap();
			return cacheMap;
		}
	},
	
	mock {

		public CcpCache getInstance() {
			CacheMock cacheMock = new CacheMock();
			return cacheMock;
		}
	}
	;

}	

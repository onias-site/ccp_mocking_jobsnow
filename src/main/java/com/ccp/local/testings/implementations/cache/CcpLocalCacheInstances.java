package com.ccp.local.testings.implementations.cache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

/**
 * Enum de provedores de DI de cache para testes locais. Oferece três variantes: {@code map}
 * (in-memory com expiração), {@code endpoint} (stub de endpoint externo) e {@code mock} (noop).
 */
public enum CcpLocalCacheInstances implements CcpInstanceProvider<CcpCache>{

	map{

		public CcpCache getInstance() {
			return new CacheMap();
		}
	},
	
	endpoint{

		public CcpCache getInstance() {
			return new CacheEndpoint();
		}
	},
	mock {

		public CcpCache getInstance() {
			return new CacheMock();
		}
	}
	;

}	

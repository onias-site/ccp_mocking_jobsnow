package com.ccp.local.testings.implementations.cache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

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

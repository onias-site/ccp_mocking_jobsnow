package com.ccp.local.testings.implementations;

import com.ccp.dependency.injection.CcpInstanceProvider;

public enum CcpLocalInstances implements CcpInstanceProvider<Object>{
	email {
		public Object getInstance() {
			LocalEmailSender localEmailSender = new LocalEmailSender();
			return localEmailSender;
		}
	},
	bucket {
		public Object getInstance() {
			LocalBucket localBucket = new LocalBucket();
			return localBucket;
		}
	}, 
	
	mensageriaSender {
		public Object getInstance() {
			LocalMensageriaSender localMensageriaSender = new LocalMensageriaSender();
			return localMensageriaSender;
		}
	};

	abstract public Object getInstance();
	
}

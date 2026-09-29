package com.ccp.local.testings.implementations;

import com.ccp.dependency.injection.CcpInstanceProvider;

/**
 * Enum of local DI providers for tests. Each constant instantiates the matching mock
 * implementation: {@code email} → {@code LocalEmailFile}, {@code bucket} → {@code LocalBucket},
 * {@code mensageriaSender} → {@code LocalMensageriaSender}.
 */
public enum CcpLocalInstances implements CcpInstanceProvider<Object>{
	email {
		public Object getInstance() {
			LocalEmailFile emailSender = new LocalEmailFile();
			return emailSender;
		}
	},
	instantMessenger {
		public Object getInstance() {
			LocalInstantMessengerFile instantMessenger = new LocalInstantMessengerFile();
			return instantMessenger;
		}
	},
	bucket {
		public Object getInstance() {
			LocalBucket localBucket = new LocalBucket();
			return localBucket;
		}
	}, 
	syncMensageriaListener {
		public Object getInstance() {
			SyncMensageriaListener localMensageriaSender = new SyncMensageriaListener();
			return localMensageriaSender;
		}
	},
	asyncMensageriaListener {
		public Object getInstance() {
			AsyncMensageriaListener localMensageriaSender = new AsyncMensageriaListener();
			return localMensageriaSender;
		}
	},
	;

	abstract public Object getInstance();
	
}

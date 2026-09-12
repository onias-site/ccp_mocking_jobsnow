package com.ccp.local.testings.implementations;

import com.ccp.dependency.injection.CcpInstanceProvider;

/**
 * Enum de provedores de DI locais para testes. Cada constante instancia a implementação
 * mock correspondente: {@code email} → {@code LocalEmailFile}, {@code bucket} → {@code LocalBucket},
 * {@code mensageriaSender} → {@code LocalMensageriaSender}.
 */
public enum CcpLocalInstances implements CcpInstanceProvider<Object>{
	email {
		public Object getInstance() {
			LocalEmailFile emailSender = new LocalEmailFile();
			return emailSender;
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

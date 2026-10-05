package com.ccp.local.testings.implementations;

import com.ccp.dependency.injection.CcpInstanceProvider;

/** Local providers for tests: each constant builds the local replacement of an external service. */
public enum CcpLocalInstances implements CcpInstanceProvider<Object>{
	/** E-mails written to files ({@code LocalEmailFile}). */
	email {
		/**
		 * Builds the e-mail sender that writes files.
		 * @return a new {@code LocalEmailFile}
		 */
		public Object getInstance() {
			LocalEmailFile emailSender = new LocalEmailFile();
			return emailSender;
		}
	},
	/** Instant messages written to files ({@code LocalInstantMessengerFile}). */
	instantMessenger {
		/**
		 * Builds the instant messenger that writes files.
		 * @return a new {@code LocalInstantMessengerFile}
		 */
		public Object getInstance() {
			LocalInstantMessengerFile instantMessenger = new LocalInstantMessengerFile();
			return instantMessenger;
		}
	},
	/** Bucket in the local file system ({@code LocalBucket}). */
	bucket {
		/**
		 * Builds the local bucket.
		 * @return a new {@code LocalBucket}
		 */
		public Object getInstance() {
			LocalBucket localBucket = new LocalBucket();
			return localBucket;
		}
	}, 
	/** Messaging that runs the processes synchronously ({@code SyncMensageriaListener}). */
	syncMensageriaListener {
		/**
		 * Builds the synchronous messaging.
		 * @return a new {@code SyncMensageriaListener}
		 */
		public Object getInstance() {
			SyncMensageriaListener localMensageriaSender = new SyncMensageriaListener();
			return localMensageriaSender;
		}
	},
	/** Messaging that runs each process in its own thread ({@code AsyncMensageriaListener}). */
	asyncMensageriaListener {
		/**
		 * Builds the asynchronous messaging.
		 * @return a new {@code AsyncMensageriaListener}
		 */
		public Object getInstance() {
			AsyncMensageriaListener localMensageriaSender = new AsyncMensageriaListener();
			return localMensageriaSender;
		}
	},
	;

	/**
	 * Builds the local implementation.
	 * @return the implementation
	 */
	abstract public Object getInstance();
	
}

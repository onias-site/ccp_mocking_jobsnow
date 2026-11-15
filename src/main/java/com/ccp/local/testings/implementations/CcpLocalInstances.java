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
			LocalEmailFile emailSender = LocalEmailFile.INSTANCE;
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
			LocalInstantMessengerFile instantMessenger = LocalInstantMessengerFile.INSTANCE;
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
			LocalBucket localBucket = LocalBucket.INSTANCE;
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
			SyncMensageriaListener localMensageriaSender = SyncMensageriaListener.INSTANCE;
			return localMensageriaSender;
		}
	},
	/** Messaging of the local REST APIs that emulates a Pub/Sub push to the local push consumer ({@code PubSubPushEmulator}). */
	pubSubPushEmulator {
		/**
		 * Builds the emulation of the Pub/Sub push.
		 * @return the {@code PubSubPushEmulator}
		 */
		public Object getInstance() {
			PubSubPushEmulator localMensageriaSender = PubSubPushEmulator.INSTANCE;
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

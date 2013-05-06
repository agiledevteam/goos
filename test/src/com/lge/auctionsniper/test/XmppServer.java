package com.lge.auctionsniper.test;

import java.io.InputStream;

import org.apache.vysper.mina.C2SEndpoint;
import org.apache.vysper.storage.StorageProviderRegistry;
import org.apache.vysper.storage.inmemory.MemoryStorageProviderRegistry;
import org.apache.vysper.xmpp.addressing.EntityImpl;
import org.apache.vysper.xmpp.authentication.AccountManagement;
import org.apache.vysper.xmpp.server.XMPPServer;

public class XmppServer {

	private XMPPServer server;

	public void start() throws Exception {
		StorageProviderRegistry providerRegistry = new MemoryStorageProviderRegistry();

		AccountManagement accountManagement = (AccountManagement) providerRegistry
				.retrieve(AccountManagement.class);
		accountManagement.addUser(EntityImpl.parseUnchecked("auction-item-54321@localhost"),
				"auction");
		accountManagement.addUser(EntityImpl.parseUnchecked("auction-item-65432@localhost"),
				"auction");
		accountManagement.addUser(EntityImpl.parseUnchecked("sniper@localhost"),
				"sniper");

		server = new XMPPServer("localhost");
		server.addEndpoint(new C2SEndpoint());
		server.setStorageProviderRegistry(providerRegistry);

		InputStream cert = XmppServer.class.getClassLoader()
				.getResourceAsStream("keystore.bks");

		server.setTLSCertificateInfo(cert, "boguspw", "BKS");

		server.start();
	}

	public void stop() {
		server.stop();
	}

}

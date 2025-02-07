package com.example.store.client;

public interface ClientItf {
	 Client registerClient(Client client);
	 Client authenticateClient(String email, String password);

}

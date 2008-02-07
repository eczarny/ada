package com.divisiblebyzero.network;

//
//  network.Client.java
//  Ada Chess
//
//  Created by Eric Czarny on April 29, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import java.net.Socket;

import org.apache.log4j.Logger;

import com.divisiblebyzero.ada.Ada;

public class Client extends Thread {
	private Socket socket;
	private byte[] buffer;
	private Ada ada;
	private Notifier notifier;
	
	private static Logger logger = Logger.getLogger(Client.class);
	
	public Client(String address, int port, Notifier notifier, Ada ada) throws Exception {
		this.buffer = new byte[5];
		
		try {
			this.socket = new Socket(address, port);
			
			this.ada = ada;
			this.notifier = notifier;
			
			logger.info("Client created, attempting to start client thread...");
			
			this.start();
		} catch (Exception e) {
			logger.error("Unable to start client.");
			
			throw new Exception("Failed connecting to" + address + ":" + port + ", " + e.getMessage());
		}
	}
	
	public void run() {
		Message message =  new Message();
		
		logger.info("Client started, attempting to connect to host...");
		
		/* Send a connection request to Host. */
		try {
			write(this.socket, (new Message(Message.Commands.CONNECT).toBytes()));
			
			read(this.socket, this.buffer);
			
			/* Parse the buffer, creating a new Message. */
			message.parseBytes(this.buffer);
			
			logger.debug("Network message received by client: " + message + ".");
			
			/* Setup request accepted, enter Client loop */
			if (message.isAccepted()) {
				logger.info("Network connection between host and client established.");
				
				/* Let Ada know we're connected. */
				this.notifier.isConnected(true);
				
				while (true) {
					while (!this.notifier.isOutgoing()) {
						/* Waiting for an outgoing message... */
					}
					
					write(this.socket, this.notifier.getMessage().toBytes());
					
					this.notifier.isOutgoing(false);
					
					read(this.socket, this.buffer);
					
					/* Parse the buffer, creating a new Message. */
					message.parseBytes(this.buffer);
					
					logger.debug("Network message received by client: " + message + ".");
					
					/* If the Client receives an accepted disconnect, tear down connection. */
					if (message.isAccepted()) {
						break;
					}
					
					/* Give Ada the new message. */
					this.notifier.setMessage(message);
					
					/* Let Ada know we have an incoming message. */
					this.notifier.isIncoming(true);
					
					/* Let the Table and Board know we are updating. */
					this.ada.getTable().networkUpdateNotification();
				}
			}
			
			this.socket.close();
		} catch (Exception e) {
			logger.error("Connection with host lost!");
		}
	}
	
	private static void read(Socket connection, byte buffer[]) throws Exception {
		connection.getInputStream().read(buffer);
	}
	
	private static void write(Socket connection, byte buffer[]) throws Exception {
		connection.getOutputStream().write(buffer);
	}
}

package com.divisiblebyzero.network;

//
//  network.Client.java
//  Ada Chess
//
//  Created by Eric Czarny on April 29, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import java.net.*;
import javax.swing.*;

import com.divisiblebyzero.ada.*;
import com.divisiblebyzero.ada.ui.*;
import com.divisiblebyzero.utilities.*;

public class Client extends Thread {
	private Socket socket;
	private byte[] buffer;
	private Ada ada;
	private Notifier notifier;
	
	public Client(String address, int port, Notifier notifier, Ada ada) throws Exception {
		this.buffer = new byte[5];
		
		try {
			this.socket = new Socket(address, port);
			
			this.ada = ada;
			this.notifier = notifier;
			
			this.start();
		} catch (Exception e) {
			Resource.getClip("/audio/Funk.aiff").start();
			
			JOptionPane.showMessageDialog(null, "Failed connecting to" + address + ":" + port,
					"Network Error", JOptionPane.ERROR_MESSAGE);
			
			throw new Exception("Failed connecting to" + address + ":" + port);
		}
	}
	
	public void run() {
		Message message =  new Message();
		
		try {
			/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
			
			/* Send a connection request to Host. */
			write(this.socket, (new Message(Message.Commands.CONNECT).toBytes()));
			
			/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
			
			read(this.socket, this.buffer);
			
			/* Parse the buffer, creating a new Message. */
			message.parseBytes(this.buffer);
			
			/* Setup request accepted, enter Client loop */
			if (message.isAccepted()) {
				/* Let Ada know we're connected. */
				this.notifier.isConnected(true);
				
				Resource.getClip("/audio/Glass.aiff").start();
				
				while (true) {
					/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
					
					while (!this.notifier.isOutgoing()) {
						/* Waiting for an outgoing message...  */
					}
					
					write(this.socket, this.notifier.getMessage().toBytes());
					
					this.notifier.isOutgoing(false);
					
					/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
					
					read(this.socket, this.buffer);
					
					/* Parse the buffer, creating a new Message. */
					message.parseBytes(this.buffer);
					
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
					
					/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
				}
			}
			
			this.socket.close();
		} catch (Exception e) {
			Resource.getClip("/audio/Funk.aiff").start();
			
			JOptionPane.showMessageDialog(null, "Lost connection with Host!", "Network Error",
					JOptionPane.ERROR_MESSAGE);
			
			this.ada.getTable().setVisible(false);
			
			new Setup(this.ada);
		}
	}
	
	private static void read(Socket connection, byte buffer[]) throws Exception {
		connection.getInputStream().read(buffer);
	}
	
	private static void write(Socket connection, byte buffer[]) throws Exception {
		connection.getOutputStream().write(buffer);
	}
}

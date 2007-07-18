package com.divisiblebyzero.network;

//
//  network.Host.java
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

public class Host extends Thread {
	private ServerSocket socket;
	private byte[] buffer;
	private Ada ada;
	private Notifier notifier;

	public Host(int port, Notifier notifier, Ada ada) throws Exception {
		this.buffer = new byte[5];

		try {
			this.socket = new ServerSocket(port);

			this.ada = ada;
			this.notifier = notifier;

			this.start();
		} catch (Exception e) {
			Resource.getClip("/audio/Funk.aiff").start();

			JOptionPane.showMessageDialog(null, "Failed binding to port " + port, "Network Error",
				JOptionPane.ERROR_MESSAGE);

			throw new Exception("Failed binding to port " + port);
		}
	}

	public void run() {
		Message message =  new Message();

		try {
			Socket connection = this.socket.accept();

			/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

			read(connection, this.buffer);

			message.parseBytes(this.buffer);

			/* Setup requested, send acceptance, and enter Host loop. */
			if (message.isConnect()) {
				/* Let Ada know we're connected. */
				this.notifier.isConnected(true);

				Resource.getClip("/audio/Glass.aiff").start();

				/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

				write(connection, (new Message(Message.Commands.ACCEPT).toBytes()));

				while (true) {
					/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

					read(connection, this.buffer);

					/* Parse the buffer, creating a new Message. */
					message.parseBytes(this.buffer);

					/* If the Host receives a disconnect, tear down connection. */
					if (message.isDisconnect()) {
						write(connection, (new Message(Message.Commands.ACCEPT).toBytes()));

						break;
					}

					/* Give Ada the new message. */
					this.notifier.setMessage(message);

					/* Let Ada know we have an incoming message. */
					this.notifier.isIncoming(true);

					/* Let the Table and Board know we are updating. */
					this.ada.getTable().networkUpdateNotification();

					/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

					while (!this.notifier.isOutgoing()) {
						/* Waiting for an outgoing message...  */
					}

					write(connection, this.notifier.getMessage().toBytes());

					this.notifier.isOutgoing(false);

					/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
				}
			}

			this.socket.close();
		} catch (Exception e) {
			Resource.getClip("/audio/Funk.aiff").start();

			JOptionPane.showMessageDialog(null, "Lost connection with Client!", "Network Error",
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
